package com.github.PublishInn.service;

import com.github.PublishInn.dto.UserDetailsEditDto;
import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.dto.mappers.AppUserMapper;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.token.ConfirmationToken;
import com.github.PublishInn.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with name %s not found";
    private final static String USER_ALREADY_EXISTS_MSG = "Email address %s already taken";

    private final static int CONFIRMATION_TOKEN_EXPIRATION_TIME = 30;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, username)));
    }

    public UserInfoDto getUserAccountInfo(String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return mapper.toUserInfoDto(user.orElseThrow( () ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, username)
                )));
    }

    public UserInfoDto getUserAccountInfo(Long id) {
        Optional<AppUser> user = userRepository.findById(id);
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return mapper.toUserInfoDto(user.orElseThrow( () ->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG, id)
                )));
    }

    public List<UserInfoDto> findAllUsers() {
        List<AppUser> users = userRepository.findAll();
        AppUserMapper mapper = Mappers.getMapper(AppUserMapper.class);
        return users
                .stream()
                .map(mapper::toUserInfoDto)
                .collect(Collectors.toList());
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = userRepository.findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException(String.format(USER_ALREADY_EXISTS_MSG, appUser.getEmail()));
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

    public void grantRoleToAppUser(String username, String role) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        user.ifPresentOrElse(appUser -> {
            appUser.setAppUserRole(AppUserRole.valueOf(role));
            userRepository.save(appUser);
        },
                () -> {
            throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
                });
    }

    public void blockUser(String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        user.ifPresentOrElse(appUser -> {
            appUser.setLocked(true);
            userRepository.save(appUser);
        },
                () -> {
                    throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
                });
    }

    public void unblockUser(String username) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        user.ifPresentOrElse(appUser -> {
                    appUser.setLocked(false);
                    userRepository.save(appUser);
                },
                () -> {
                    throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
                });
    }

    public void editUserAccountDetails(String username, UserDetailsEditDto model) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        user.ifPresentOrElse(appUser -> {
                    appUser.setEmail(model.getMailAddress());
                    appUser.setAppUserRole(AppUserRole.valueOf(model.getUserRole()));
                    userRepository.save(appUser);
                },
                () -> {
                    throw new UsernameNotFoundException(USER_NOT_FOUND_MSG);
                });
    }
}
