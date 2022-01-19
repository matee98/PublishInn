package com.github.PublishInn.service;

import com.github.PublishInn.dto.CommentDetailsDto;
import com.github.PublishInn.dto.EditCommentDto;
import com.github.PublishInn.dto.NewCommentDto;
import com.github.PublishInn.dto.mappers.CommentMapper;
import com.github.PublishInn.exceptions.CommentException;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.model.entity.AppUser;
import com.github.PublishInn.model.entity.Comment;
import com.github.PublishInn.model.entity.Work;
import com.github.PublishInn.model.entity.enums.AppUserRole;
import com.github.PublishInn.model.repository.CommentRepository;
import com.github.PublishInn.model.repository.UserRepository;
import com.github.PublishInn.model.repository.WorkRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    public CommentDetailsDto getCommentById(Long commentId) throws CommentException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::notFound);
        String username;
        if (userRepository.existsById(comment.getUserId())) {
            username = userRepository.getById(comment.getUserId()).getUsername();
        } else {
            username = "null";
        }
        CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
        CommentDetailsDto result = mapper.toCommentDetailsDto(comment);
        result.setUsername(username);
        return result;
    }

    public List<CommentDetailsDto> findAll() {
        CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
        return commentRepository.findAll()
                .stream()
                .map(comment -> {
                    CommentDetailsDto result = mapper.toCommentDetailsDto(comment);
                    Optional<AppUser> user = userRepository.findById(comment.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<CommentDetailsDto> findAllByWorkId(Long workId) {
        CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
        return commentRepository.findAllByWorkIdEquals(workId)
                .stream()
                .map(comment -> {
                    CommentDetailsDto result = mapper.toCommentDetailsDto(comment);
                    Optional<AppUser> user = userRepository.findById(comment.getUserId());
                    user.ifPresent(appUser -> result.setUsername(appUser.getUsername()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    public List<CommentDetailsDto> findAllByUsername(String username) throws UserException {
        CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
        AppUser user = userRepository.findByUsername(username).orElseThrow(UserException::notFound);
        return commentRepository.findAllByUserIdEquals(user.getId())
                .stream()
                .map(comment -> {
                    CommentDetailsDto result = mapper.toCommentDetailsDto(comment);
                    result.setUsername(username);
                    return result;
                })
                .collect(Collectors.toList());
    }

    public void addComment(NewCommentDto model, Principal principal) throws UserException, WorkException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        Work work = workRepository.findById(model.getWorkId()).orElseThrow(WorkException::notFound);

        Comment comment = new Comment();
        comment.setUserId(user.getId());
        comment.setWorkId(work.getId());
        comment.setText(model.getText());
        comment.setVisible(true);
        comment.setCreatedBy(user.getId());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Principal principal) throws UserException, CommentException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::notFound);
        if (!Objects.equals(comment.getUserId(), user.getId())) {
            throw CommentException.accessForbidden();
        }
        commentRepository.delete(comment);
    }

    public void editComment(EditCommentDto model, Principal principal) throws UserException, CommentException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        Comment comment = commentRepository.findById(model.getCommentId()).orElseThrow(CommentException::notFound);
        if (!Objects.equals(comment.getUserId(), user.getId())) {
            throw CommentException.accessForbidden();
        }
        comment.setText(model.getText());
        comment.setModifiedBy(user.getId());
        commentRepository.save(comment);
    }

    public void changeCommentVisibility(Long commentId, Principal principal) throws UserException, CommentException {
        AppUser user = userRepository.findByUsername(principal.getName()).orElseThrow(UserException::notFound);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::notFound);
        if (user.getAppUserRole() != AppUserRole.MODERATOR) {
            throw CommentException.accessForbidden();
        }
        comment.setVisible(!comment.isVisible());
        comment.setModifiedBy(user.getId());
        commentRepository.save(comment);
    }
}
