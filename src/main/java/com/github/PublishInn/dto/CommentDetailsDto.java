package com.github.PublishInn.dto;

import lombok.Setter;

import java.time.LocalDateTime;

@Setter
public class CommentDetailsDto {
    private String username;
    private Long workId;
    private String text;
    private boolean visible;
    private LocalDateTime createdOn;
}
