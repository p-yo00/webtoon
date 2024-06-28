package com.yo.webtoon.model.annotation;

import com.yo.webtoon.validator.ValidRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidRoleValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {

    String message() default "유효하지 않은 권한입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
