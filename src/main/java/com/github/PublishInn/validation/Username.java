package com.github.PublishInn.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 3, max = 19, message = "validation.username.size")
public @interface Username {
    String message() default "validation.username";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
