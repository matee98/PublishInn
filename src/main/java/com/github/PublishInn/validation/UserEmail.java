package com.github.PublishInn.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Size(min = 4, max = 127, message = "validation.email.size")
@Email(message = "validation.email.pattern")
public @interface UserEmail {
    String message() default "validation.email.pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
