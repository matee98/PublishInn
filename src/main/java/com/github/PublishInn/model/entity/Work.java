package com.github.PublishInn.model.entity;

import com.github.PublishInn.model.entity.enums.WorkType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
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

    @Getter
    @Setter
    @NotBlank
    @Size(min = 1, max = 63)
    @Column(nullable = false)
    private String title;

    @Getter
    @Setter
    @NotBlank
    @Size(min = 1)
    @Column(nullable = false)
    private String text;

    @Getter
    @Setter
    @Min(value = 1)
    @Max(value = 10)
    @Digits(integer = 1, fraction = 2)
    private BigDecimal rating;

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
