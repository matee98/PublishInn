package com.github.PublishInn.unitTests.repository;

import com.github.PublishInn.model.entity.Comment;
import com.github.PublishInn.model.repository.CommentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    private Comment comment = new Comment();
    private Comment comment2 = new Comment();
    private Comment comment3 = new Comment();

    @BeforeAll
    void setup() {
        comment.setUserId(2L);
        comment.setWorkId(3L);
        comment.setVisible(true);
        comment.setText("Może być");
        comment.setCreatedBy(2L);

        comment2.setUserId(2L);
        comment2.setWorkId(4L);
        comment2.setVisible(true);
        comment2.setText("Fajne, 7/10");
        comment2.setCreatedBy(2L);

        comment3.setUserId(1L);
        comment3.setWorkId(3L);
        comment3.setVisible(true);
        comment3.setText("Niezłe");
        comment3.setCreatedBy(1L);

        commentRepository.save(comment);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
    }

    @Test
    void findAllByUserIdEqualsTest() {
        List<Comment> comments = commentRepository.findAllByUserIdEquals(2L);

        assertThat(comments.size()).isEqualTo(2);

        Comment first = comments.stream().filter(comment -> comment.getId().equals(1L)).findFirst().orElseThrow();

        assertThat(first.getUserId()).isEqualTo(comment.getUserId());
        assertThat(first.getWorkId()).isEqualTo(comment.getWorkId());
        assertThat(first.getText()).isEqualTo(comment.getText());
        assertThat(first.isVisible()).isEqualTo(comment.isVisible());
    }

    @Test
    void findAllByWorkIdEqualsTest() {
        List<Comment> comments = commentRepository.findAllByWorkIdEquals(3L);

        assertThat(comments.size()).isEqualTo(2);

        Comment first = comments.stream().filter(comment -> comment.getId().equals(3L)).findFirst().orElseThrow();

        assertThat(first.getUserId()).isEqualTo(comment3.getUserId());
        assertThat(first.getWorkId()).isEqualTo(comment3.getWorkId());
        assertThat(first.getText()).isEqualTo(comment3.getText());
        assertThat(first.isVisible()).isEqualTo(comment3.isVisible());
    }
}
