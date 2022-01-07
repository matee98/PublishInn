package com.github.PublishInn.controller;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.service.WorkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/works")
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public void saveWork(@RequestBody @Valid WorkSaveDto model, Principal principal) {
        workService.saveWork(model, principal);
    }

    @GetMapping("/details")
    public List<WorkDetailsDto> findAll() {
        return workService.findAll();
    }

    @GetMapping()
    public List<WorkInfoDto> findAllWorkInfo(@RequestParam(value="type", required = false) String type) {
        if (type == null) {
            return workService.findAllWorkInfo();
        } else if (type.equals("prose")) {
            return workService.findProseWorkInfo();
        } else {
            return workService.findWorkInfo(type);
        }
    }

    @GetMapping("/{id}")
    public WorkDetailsDto findById(@PathVariable Long id) {
        return workService.findById(id);
    }
}
