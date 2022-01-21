package com.github.PublishInn.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 8, max = 64, message = "validation.password.size")
@Pattern(regexp = RegEx.PASSWORD, message = "validation.password.pattern")
public @interface Password {
    String message() default "validation.password.size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
