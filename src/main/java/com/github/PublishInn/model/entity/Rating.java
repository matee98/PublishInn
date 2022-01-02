package com.github.PublishInn.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
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

    @Getter
    @Setter
    @Min(value = 1)
    @Max(value = 10)
    @NotNull
    private short rate;

    @ManyToOne(cascade = CascadeType.REFRESH, optional = false)
    @Getter
    @Setter
    private Work work;

    public Rating(short rate, Work work) {
        this.rate = rate;
        this.work = work;
    }

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
