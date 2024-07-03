package com.yo.webtoon.validator;

import com.yo.webtoon.model.annotation.ValidRole;
import com.yo.webtoon.model.constant.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValidRoleValidator implements ConstraintValidator<ValidRole, String> {

    private static final List<String> validRoles = Arrays.stream(Role.values()).map(Enum::name)
        .toList();


    @Override
    public boolean isValid(String role, ConstraintValidatorContext context) {
        return validRoles.contains(role);
    }
}
