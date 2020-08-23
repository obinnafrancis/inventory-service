package com.vlad.tech.inventoryservice.utils.validators;

import com.vlad.tech.inventoryservice.utils.Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValidationImpl implements ConstraintValidator<EnumValidator, String> {
    List<String> valueList;
    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        Enum[] enums = enumClass.getEnumConstants();
        for (Enum enumVal : enums){
            valueList.add(enumVal.name().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null) return true;
        if(Utils.isNullOrEmpty(s)) return true;
        return valueList.contains(s.toUpperCase());
    }
}
