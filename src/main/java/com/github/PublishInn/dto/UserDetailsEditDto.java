package com.github.PublishInn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDetailsEditDto {
    private String mailAddress;
    private String userRole;
}
