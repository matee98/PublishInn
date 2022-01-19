package com.github.PublishInn.dto.mappers;

import com.github.PublishInn.dto.CommentDetailsDto;
import com.github.PublishInn.model.entity.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {
    CommentDetailsDto toCommentDetailsDto(Comment comment);
}
