package com.github.PublishInn.model.entity;

import com.github.PublishInn.validation.CommentText;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends AbstractEntity {

    @Id
    @SequenceGenerator(
            name = "comment_sequence",
            sequenceName = "comment_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comment_sequence"
    )
    private Long id;

    @NotBlank
    @CommentText
    private String text;

    @NotNull
    private boolean visible;

    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Long workId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Long userId;
}
