package com.github.PublishInn.model.repository;

import com.github.PublishInn.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserIdEquals(Long userId);
    List<Comment> findAllByWorkIdEquals(Long workId);
}
