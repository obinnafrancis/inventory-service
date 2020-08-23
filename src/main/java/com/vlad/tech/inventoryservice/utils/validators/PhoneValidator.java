package com.vlad.tech.inventoryservice.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidationImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface PhoneValidator {
    String locale() default "";
    String message() default "Invalid Phone-Number Format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
