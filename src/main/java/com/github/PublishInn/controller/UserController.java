package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserDetailsEditDto;
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

    @GetMapping("/{username}")
    public UserInfoDto getUserAccountInfo(@PathVariable String username) {
        return userService.getUserAccountInfo(username);
    }

    @PatchMapping("/block/{username}")
    public void blockUser(@PathVariable String username) {
        userService.blockUser(username);
    }

    @PatchMapping("/unblock/{username}")
    public void unblockUser(@PathVariable String username) {
        userService.unblockUser(username);
    }

    @PutMapping("/edit/{username}")
    public void editUserAccountDetails(@PathVariable String username, @RequestBody UserDetailsEditDto model) {
        userService.editUserAccountDetails(username, model);
    }
}
