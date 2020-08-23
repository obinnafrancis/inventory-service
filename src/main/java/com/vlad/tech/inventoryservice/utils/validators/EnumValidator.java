package com.vlad.tech.inventoryservice.utils.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidationImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ReportAsSingleViolation
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid Value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
