package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserDetailsEditDto;
import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.dto.UserShortProfileDto;
import com.github.PublishInn.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final AppUserService userService;

    @GetMapping("/admin")
    public List<UserInfoDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/admin/{username}")
    public UserInfoDto getUserAccountInfo(@PathVariable String username) {
        return userService.getUserAccountInfo(username);
    }

    @PatchMapping("/admin/block/{username}")
    public void blockUser(@PathVariable String username) {
        userService.blockUser(username);
    }

    @PatchMapping("/admin/unblock/{username}")
    public void unblockUser(@PathVariable String username) {
        userService.unblockUser(username);
    }

    @PutMapping("/admin/edit/{username}")
    public void editUserAccountDetails(@PathVariable String username, @RequestBody UserDetailsEditDto model) {
        userService.editUserAccountDetails(username, model);
    }

    @GetMapping("/profile/short/{username}")
    public UserShortProfileDto getUserShortProfile(@PathVariable String username){
        return userService.getShortProfileByUsername(username);
    }
}
