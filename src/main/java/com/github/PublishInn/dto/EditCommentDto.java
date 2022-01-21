package com.github.PublishInn.dto;

import com.github.PublishInn.validation.CommentText;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class EditCommentDto {
    @NotNull
    private Long commentId;
    @CommentText
    private String text;
}
