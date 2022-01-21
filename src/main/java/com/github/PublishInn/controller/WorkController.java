package com.github.PublishInn.controller;

import com.github.PublishInn.dto.WorkDetailsDto;
import com.github.PublishInn.dto.WorkInfoDto;
import com.github.PublishInn.dto.WorkSaveDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.service.WorkService;
import com.github.PublishInn.validation.Username;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("api/works")
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public void saveWork(@RequestBody @Valid WorkSaveDto model, Principal principal) {
        try {
            workService.saveWork(model, principal);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/moderator/all")
    public List<WorkDetailsDto> findAll() {
        return workService.findAll();
    }

    @GetMapping("/moderator")
    public List<WorkInfoDto> findAllWorkInfo(@RequestParam(value="type", required = false) String type) {
        if (type == null) {
            return workService.findAllWorkInfo();
        } else if (type.equals("prose")) {
            return workService.findProseWorkInfo(true);
        } else {
            return workService.findWorkInfo(type, true);
        }
    }

    @GetMapping("/moderator/blocked")
    public List<WorkInfoDto> findAllBlockedWorkInfo(@RequestParam(value="type", required = false) String type) {
        return workService.findAllBlockedWorkInfo(Objects.requireNonNullElse(type, ""));
    }

    @PatchMapping("/moderator/block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void blockWorkById(@PathVariable Long id, Principal principal) {
        try {
            workService.blockWorkById(id, principal);
        } catch (WorkException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/moderator/unblock/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unblockWorkById(@PathVariable Long id, Principal principal) {
        try {
            workService.unblockWorkById(id, principal);
        } catch (WorkException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping()
    public List<WorkInfoDto> findAcceptedWorkInfo(@RequestParam(value="type", required = false) String type) {
        if (type == null) {
            return workService.findAllWorkInfo();
        } else if (type.equals("prose")) {
            return workService.findProseWorkInfo(false);
        } else {
            return workService.findWorkInfo(type, false);
        }
    }

    @GetMapping("/user/{username}")
    public List<WorkInfoDto> findWorkInfoByUsername(@PathVariable @Valid @Username String username) {
        try {
            return workService.findWorksByUsername(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<WorkInfoDto> searchWorks(@RequestParam(value = "name") @Valid @Username String name) {
        return workService.searchWorks(name);
    }

    @GetMapping("/details/{id}")
    public WorkDetailsDto findById(@PathVariable Long id, Principal principal) {
        try {
            return workService.findById(id, principal);
        } catch (WorkException e) {
            if (Objects.equals(e.getMessage(), WorkException.ACCESS_FORBIDDEN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            }
        }
    }

    @GetMapping(
            path = "/convert/pdf/{id}",
            produces = "application/pdf"
    )
    public void getAsPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        try {
            workService.getWorkAsPdf(id, response);
        } catch (WorkException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
