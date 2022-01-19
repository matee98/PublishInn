package com.github.PublishInn.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentDetailsDto {
    private String username;
    private Long workId;
    private String text;
    private boolean visible;
    private LocalDateTime createdOn;
}
