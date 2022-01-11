package com.github.PublishInn.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Rating extends AbstractEntity{

    @Id
    @SequenceGenerator(
            name = "rating_sequence",
            sequenceName = "rating_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rating_sequence"
    )
    private Long id;

    @Min(value = 1)
    @Max(value = 10)
    @NotNull
    private short rate;

    @JoinColumn(name = "work_id", referencedColumnName = "id")
    private Long workId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Long userId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.toString()
                + "id: "
                + id;
    }
}
