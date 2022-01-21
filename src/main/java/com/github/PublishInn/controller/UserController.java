package com.github.PublishInn.controller;

import com.github.PublishInn.dto.UserDetailsEditDto;
import com.github.PublishInn.dto.UserInfoDto;
import com.github.PublishInn.dto.UserShortProfileDto;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.service.AppUserService;
import com.github.PublishInn.validation.Username;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    public UserInfoDto getUserAccountInfo(@PathVariable @Valid @Username String username) {
        try {
            return userService.getUserAccountInfo(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/admin/block/{username}")
    public void blockUser(@PathVariable @Valid @Username String username) {
        try {
            userService.blockUser(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/admin/unblock/{username}")
    public void unblockUser(@PathVariable @Valid @Username String username) {
        try {
            userService.unblockUser(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/edit/{username}")
    public void editUserAccountDetails(@PathVariable @Valid @Username String username,
                                       @RequestBody @Valid UserDetailsEditDto model) {
        try {
            userService.editUserAccountDetails(username, model);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/profile/short/{username}")
    public UserShortProfileDto getUserShortProfile(@PathVariable @Valid @Username String username){
        try {
            return userService.getShortProfileByUsername(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
