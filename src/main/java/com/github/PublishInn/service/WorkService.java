package com.github.PublishInn.service;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.model.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkService {
    private final static String USER_NOT_FOUND_MSG = "User with name %s not found";
    private final static String WORK_NOT_FOUND_MSG = "Work with id %s not found";

    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    public void saveWork (WorkSaveDto model, Principal principal){
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Work work = mapper.fromWorkSaveDto(model);
        Optional<AppUser> user = userRepository.findByUsername(principal.getName());
        user.ifPresentOrElse(appUser -> {
            work.setUserId(appUser.getId());
            work.setCreatedBy(appUser.getId());
            work.setStatus(WorkStatus.ACCEPTED);
        }, () -> {
            throw new UsernameNotFoundException(
                    String.format(USER_NOT_FOUND_MSG, principal.getName()));
        });
        workRepository.save(work);
    }

    public List<WorkDetailsDto> findAll() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        return workRepository.findAll()
                .stream()
                .map(work -> {
                    WorkDetailsDto result = mapper.toDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> findAllWorkInfo() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        return workRepository.findAll()
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> findWorkInfo(String type, boolean blocked) {
        WorkType workType = WorkType.valueOf(type.toUpperCase(Locale.ROOT));

        List<Work> works = workRepository.findAllByTypeEquals(workType);

        if (!blocked) {
            works = filterBlockedWorks(works);
        }

        return mapWorksToDtos(works);
    }

    public List<WorkInfoDto> findProseWorkInfo(boolean blocked) {
        List<Work> works = workRepository.findAllByTypeIsNotLike(WorkType.POEM);

        if (!blocked) {
            works = filterBlockedWorks(works);
        }

        return mapWorksToDtos(works);
    }

    public WorkDetailsDto findById(Long id, Principal principal) throws WorkException {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<Work> work = workRepository.findById(id);
        WorkDetailsDto result = mapper.toDto(work.orElseThrow(() ->
                new NoSuchElementException(
                        String.format(WORK_NOT_FOUND_MSG, id)
                )));
        Optional<AppUser> caller;
        if (principal != null) {
            caller = userRepository.findByUsername(principal.getName());
        } else {
            caller = Optional.empty();
        }
        if (result.getStatus().equals(WorkStatus.BLOCKED)) {
            if (caller.isEmpty() || !caller.get().getAppUserRole().equals(AppUserRole.MODERATOR)) {
                throw WorkException.accessForbidden();
            }
        }
        userRepository.findById(work.get().getUserId()).ifPresent(appUser -> {
            result.setUsername(appUser.getUsername());
        });
        return result;
    }

    public List<WorkInfoDto> findWorksByUsername(String username) {
        List<Work> resultWorks;
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            resultWorks = workRepository.findAllByUserIdEquals(user.get().getId());
        } else {
            throw new UsernameNotFoundException(
                    String.format(USER_NOT_FOUND_MSG, username));
        }

        return resultWorks
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    result.setUsername(username);
                    return result;
                })
                .collect(Collectors.toList());
    }

    public void blockWorkById(Long workId, Principal principal) {
        Optional<Work> work = workRepository.findById(workId);
        work.ifPresentOrElse(w -> {
            w.setStatus(WorkStatus.BLOCKED);
            w.setModifiedBy(userRepository.findByUsername(principal.getName()).get().getId());
            workRepository.save(w);
        }, () -> {
            throw new NoSuchElementException(WORK_NOT_FOUND_MSG);
        });
    }

    public void unblockWorkById(Long workId, Principal principal) {
        Optional<Work> work = workRepository.findById(workId);
        work.ifPresentOrElse(w -> {
            w.setStatus(WorkStatus.ACCEPTED);
            w.setModifiedBy(userRepository.findByUsername(principal.getName()).get().getId());
            workRepository.save(w);
        }, () -> {
            throw new NoSuchElementException(WORK_NOT_FOUND_MSG);
        });
    }

    private List<Work> filterBlockedWorks(List<Work> works) {
        return works
                .stream()
                .filter(w -> w.getStatus().equals(WorkStatus.ACCEPTED))
                .collect(Collectors.toList());
    }

    private List<WorkInfoDto> mapWorksToDtos(List<Work> works) {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);

        return works
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }
}
