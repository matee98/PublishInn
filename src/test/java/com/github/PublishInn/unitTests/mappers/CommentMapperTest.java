package com.github.PublishInn.unitTests.mappers;

import com.github.PublishInn.dto.CommentDetailsDto;
import com.github.PublishInn.dto.mappers.CommentMapper;
import com.github.PublishInn.model.entity.Comment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);
    private final Comment comment = new Comment();

    @BeforeAll
    void setup() {
        comment.setId(12L);
        comment.setWorkId(2L);
        comment.setText("Bardzo polecam");
        comment.setUserId(1L);
        comment.setVisible(true);
        comment.setCreatedBy(1L);
    }

    @Test
    void toCommentDetailsDtoTest() {
        CommentDetailsDto dto = mapper.toCommentDetailsDto(comment);

        assertThat(dto.getId()).isEqualTo(comment.getId());
        assertThat(dto.getWorkId()).isEqualTo(comment.getWorkId());
        assertThat(dto.getText()).isEqualTo(comment.getText());
        assertThat(dto.getUsername()).isNull();
    }
}
