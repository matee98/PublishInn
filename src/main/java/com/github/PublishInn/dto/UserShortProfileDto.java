package com.github.PublishInn.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserShortProfileDto {
    private String username;
    private String userRole;
    private int worksCount;
    private LocalDateTime createdOn;
}
