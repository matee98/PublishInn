package com.github.PublishInn.service;

import com.github.PublishInn.dto.*;
import com.github.PublishInn.dto.mappers.AppUserMapper;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.OneTimeCode;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.token.ConfirmationToken;
import com.github.PublishInn.model.repository.OneTimeCodeRepository;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.utils.EmailBuilder;
import com.github.PublishInn.utils.EmailService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private static final String RESET_PASSWORD_LINK = "https://publish-inn.herokuapp.com/password/reset/confirm/";

    private final static int CONFIRMATION_TOKEN_EXPIRATION_TIME = 30;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final OneTimeCodeRepository codeRepository;
    private final EmailService emailService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserException::notFound);
    }

    public UserInfoDto getUserAccountInfo(String username) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return mapper.toUserInfoDto(user.orElseThrow(UserException::notFound));
    }

    public UserInfoDto getUserAccountInfo(Long id) throws UserException {
        Optional<AppUser> user = userRepository.findById(id);
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return mapper.toUserInfoDto(user.orElseThrow(UserException::notFound));
    }

    public List<UserInfoDto> findAllUsers() {
        List<AppUser> users = userRepository.findAll();
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return users
                .stream()
                .map(mapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    public String signUpUser(AppUser appUser) throws UserException {
        Optional<AppUser> byEmail = userRepository.findByEmail(appUser.getEmail());
        Optional<AppUser> byUsername = userRepository.findByUsername(appUser.getUsername());

        if (byEmail.isPresent()) {
            throw UserException.emailExists();
        }

        if (byUsername.isPresent()) {
            throw UserException.usernameExists();
        }

        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(CONFIRMATION_TOKEN_EXPIRATION_TIME),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public void enableAppUser(String email) {
        Optional<AppUser> userToEnable = userRepository.findByEmail(email);
        userToEnable.ifPresent(user -> {
            user.setEnabled(true);
            userRepository.save(user);
        });
    }

    public void grantRoleToAppUser(String username, String role) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            user.get().setAppUserRole(AppUserRole.valueOf(role));
            userRepository.save(user.get());
        } else {
            throw UserException.notFound();
        }
    }

    public void blockUser(String username) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setLocked(true);
            userRepository.save(user.get());
        } else {
            throw UserException.notFound();
        }
    }

    public void unblockUser(String username) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setLocked(false);
            userRepository.save(user.get());
        } else {
            throw UserException.notFound();
        }
    }

    public void editUserAccountDetails(String username, UserDetailsEditDto model) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            user.get().setEmail(model.getMailAddress());
            user.get().setAppUserRole(AppUserRole.valueOf(model.getUserRole()));
            userRepository.save(user.get());
        } else {
            throw UserException.notFound();
        }
    }

    public UserShortProfileDto getShortProfileByUsername(String username) throws UserException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        UserShortProfileDto result;

        if (user.isPresent()){
            result = mapper.toUserShortProfileDto(user.get());
            List<Work> userWorks = user.get().getWorks();
            BigDecimal worksRatingSum = BigDecimal.ZERO;
            int count = 0;
            for (Work work : userWorks) {
                if (work.getRating() != null && work.getStatus() == WorkStatus.ACCEPTED) {
                    worksRatingSum = worksRatingSum.add(work.getRating());
                    count++;
                }
            }
            result.setWorksCount(userWorks.size());
            if (userWorks.size() != 0) {
                result.setRatingAverage(worksRatingSum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP));
            }
        } else {
            throw UserException.notFound();
        }

        return result;
    }

    public void sendResetPasswordCode(String email) throws UserException {
        AppUser user = userRepository.findByEmail(email).orElseThrow(UserException::notFound);
        String code = UUID.randomUUID().toString();
        OneTimeCode token = new OneTimeCode(code, user);
        codeRepository.save(token);
        emailService.send(
                user.getEmail(),
                EmailBuilder.buildResetPasswordEmail(
                        user.getUsername(),
                        RESET_PASSWORD_LINK + code),
                "Zresetuj swoje hasło");
    }

    public void resetPassword(ResetPasswordDto model) {
        OneTimeCode code = codeRepository.findByCode(model.getCode()).orElseThrow();
        AppUser user = code.getAppUser();
        user.setPassword(bCryptPasswordEncoder.encode(model.getNewPassword()));
        codeRepository.delete(code);
    }

    public void selfChangePassword(ChangePasswordDto model, Principal principal) throws UserException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        if (!bCryptPasswordEncoder.matches(model.getOldPassword(), user.getPassword())) {
            throw UserException.passwordNotMatch();
        }
        user.setPassword(bCryptPasswordEncoder.encode(model.getNewPassword()));
        userRepository.save(user);
    }

    public void selfChangeEmail(UserEmailDto model, Principal principal) throws UserException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        Optional<AppUser> byEmail = userRepository.findByEmail(model.getEmail());
        if (byEmail.isPresent()) {
            throw UserException.emailExists();
        }
        user.setEmail(model.getEmail());
        userRepository.save(user);
    }
}
