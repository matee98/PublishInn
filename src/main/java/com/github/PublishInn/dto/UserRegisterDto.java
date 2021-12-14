package com.github.PublishInn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;

}
