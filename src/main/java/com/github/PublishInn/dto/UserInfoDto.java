package com.github.PublishInn.dto;

import com.github.PublishInn.model.entity.AppUserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDto {
    private String username;
    private String email;
    private AppUserRole appUserRole;
    private boolean locked;
    private boolean enabled;
}
