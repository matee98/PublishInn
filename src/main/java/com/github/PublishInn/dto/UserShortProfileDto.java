package com.github.PublishInn.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserShortProfileDto {
    private String username;
    private String userRole;
    private int worksCount;
    private LocalDateTime createdOn;
    private BigDecimal ratingAverage;
}
