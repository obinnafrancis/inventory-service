package com.vlad.tech.inventoryservice.utils.validators;

import org.passay.*;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordPolicyValidator implements ConstraintValidator<PasswordPolicy, String> {
    @Value("${min.password.length}")
    int min;

    private String message;

    List<String> valueList;

    @Override
    public void initialize(PasswordPolicy passwordPolicy) {
        this.message= passwordPolicy.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        PasswordValidator passwordValidator = new PasswordValidator(Arrays.asList(
                new LengthRule(min,30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1)));
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
