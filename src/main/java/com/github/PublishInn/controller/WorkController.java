package com.github.PublishInn.controller;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.dto.mappers.WorkMapper;
import com.github.PublishInn.service.WorkService;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("api/works")
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public void saveWork(@RequestBody @Valid WorkSaveDto model, Principal principal) {
        workService.saveWork(model, principal);
    }

    @GetMapping
    public List<WorkDetailsDto> findAll() {
        return workService.findAll();
    }

    @GetMapping("/{id}")
    public WorkDetailsDto findById(@PathVariable Long id) {
        return workService.findById(id);
    }
}
