package com.github.PublishInn.controller;

import com.github.PublishInn.dto.CommentDetailsDto;
import com.github.PublishInn.dto.EditCommentDto;
import com.github.PublishInn.dto.NewCommentDto;
import com.github.PublishInn.exceptions.CommentException;
import com.github.PublishInn.exceptions.UserException;
import com.github.PublishInn.exceptions.WorkException;
import com.github.PublishInn.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDetailsDto> findAll() {
        return commentService.findAll();
    }

    @GetMapping("/find/{id}")
    public CommentDetailsDto findById(@PathVariable Long id) {
        try {
            return commentService.getCommentById(id);
        } catch (CommentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{username}")
    public List<CommentDetailsDto> findAllByUsername(@PathVariable String username) {
        try {
            return commentService.findAllByUsername(username);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/work/{workId}")
    public List<CommentDetailsDto> findAllByWorkId(@PathVariable Long workId) {
        return commentService.findAllByWorkId(workId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addComment(@RequestBody NewCommentDto model, Principal principal) {
        try {
            commentService.addComment(model, principal);
        } catch (WorkException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public void editComment(@RequestBody EditCommentDto model, Principal principal) {
        try {
            commentService.editComment(model, principal);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (CommentException e) {
            if (Objects.equals(e.getMessage(), CommentException.ACCESS_FORBIDDEN)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            } else if (Objects.equals(e.getMessage(), CommentException.NOT_FOUND)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id, Principal principal) {
        try {
            commentService.deleteComment(id, principal);
        } catch (UserException | CommentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/changeVisibility/{commentId}")
    public void changeVisibility(@PathVariable Long commentId, Principal principal) {
        try {
            commentService.changeCommentVisibility(commentId, principal);
        } catch (UserException | CommentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
