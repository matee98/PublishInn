package com.github.PublishInn.dto;

import com.github.PublishInn.validation.CommentText;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NewCommentDto {
    @NotNull
    private Long workId;
    @CommentText
    private String text;
}
