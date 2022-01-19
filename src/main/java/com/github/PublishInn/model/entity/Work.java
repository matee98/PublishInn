package com.github.PublishInn.model.entity;

import com.github.PublishInn.model.entity.enums.WorkStatus;
import com.github.PublishInn.model.entity.enums.WorkType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Work extends AbstractEntity {

    @Id
    @SequenceGenerator(
            name = "work_sequence",
            sequenceName = "work_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "work_sequence"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @NotBlank
    @Size(min = 1, max = 63)
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Size(min = 1)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Long userId;

    @OneToMany(mappedBy = "workId")
    private List<Rating> ratingList;

    @OneToMany(mappedBy = "workId")
    private List<Comment> comments;

    @Min(value = 1)
    @Max(value = 10)
    @Digits(integer = 1, fraction = 2)
    private BigDecimal rating;

    @Enumerated(EnumType.STRING)
    @NotNull
    private WorkStatus status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.toString() +
                "id: " +
                id;
    }
}
