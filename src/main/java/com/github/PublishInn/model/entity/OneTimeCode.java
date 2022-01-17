package com.github.PublishInn.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OneTimeCode {
    @SequenceGenerator(
            name = "code_sequence",
            sequenceName = "code_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "code_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String code;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public OneTimeCode(String code, AppUser appUser) {
        this.code = code;
        this.appUser = appUser;
    }
}
