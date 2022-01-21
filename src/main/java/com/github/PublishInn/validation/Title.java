package com.github.PublishInn.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "validation.title.blank")
@Size(min = 1, max = 63, message = "validation.title.bounds")
public @interface Title {
    String message() default "validation.title.bounds";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
