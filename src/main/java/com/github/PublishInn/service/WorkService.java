package com.github.PublishInn.service;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.entity.enums.WorkStatus;
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
            work.setCreatedBy(appUser);
            work.setStatus(WorkStatus.WAITING);
        }, () -> {
            throw new UsernameNotFoundException(
                    String.format(USER_NOT_FOUND_MSG, principal.getName()));
        });

        if (user.get().getAppUserRole() != AppUserRole.USER) {
            throw new IllegalStateException();
        }
        //work.setRating(BigDecimal.ZERO);
        workRepository.save(work);
    }

    public List<WorkDetailsDto> findAll() {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        return workRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public WorkDetailsDto findById(Long id) {
        WorkMapper mapper = Mappers.getMapper(WorkMapper.class);
        Optional<Work> work = workRepository.findById(id);
        return mapper.toDto(work.orElseThrow(() ->
            new NoSuchElementException(
                    String.format(WORK_NOT_FOUND_MSG, id)
            )));
    }
}
