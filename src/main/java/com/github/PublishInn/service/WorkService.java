package com.github.PublishInn.service;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.model.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            work.setStatus(WorkStatus.WAITING);
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

    public List<WorkInfoDto> findWorkInfo(String type) {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        WorkType workType = WorkType.valueOf(type.toUpperCase(Locale.ROOT));

        return workRepository.findAllByTypeEquals(workType)
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<WorkInfoDto> findProseWorkInfo() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);

        return workRepository.findAllByTypeIsNotLike(WorkType.POEM)
                .stream()
                .map(work -> {
                    WorkInfoDto result = mapper.toWorkInfoDto(work);
                    Optional<AppUser> user = userRepository.findById(work.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public WorkDetailsDto findById(Long id) {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<Work> work = workRepository.findById(id);
        WorkDetailsDto result = mapper.toDto(work.orElseThrow(() ->
                new NoSuchElementException(
                        String.format(WORK_NOT_FOUND_MSG, id)
                )));
        userRepository.findById(work.get().getUserId()).ifPresent(appUser -> {
            result.setUsername(appUser.getUsername());
        });
        return result;
    }
}
