package com.github.PublishInn.dto;

import com.github.PublishInn.validation.UserEmail;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDetailsEditDto {
    @UserEmail
    private String mailAddress;
    @NotNull
    private String userRole;
}
