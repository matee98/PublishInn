package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final AppUserService userService;

    @GetMapping
    public List<UserInfoDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserInfoDto getUserAccountInfo(@PathVariable Long id) {
        return userService.getUserAccountInfo(id);
    }

    @PatchMapping("/grantRole/{id}")
    public void grantRoleToUser(@PathVariable Long id, @RequestBody String role) {
        userService.grantRoleToAppUser(id, role);
    }
}
