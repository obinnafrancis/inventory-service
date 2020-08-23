package com.vlad.tech.inventoryservice.utils.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vlad.tech.inventoryservice.utils.Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class PhoneValidationImpl implements ConstraintValidator<PhoneValidator, String> {
    List<String> valueList;
    private static final String DEFAULT_REGION_CODE = "NG";

    @Override
    public void initialize(PhoneValidator contact) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s == null || Utils.isNullOrEmpty(s)) {
            return true;
        } else if (Utils.isNullOrEmpty(s)) {
            return true;
        } else {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber number = phoneNumberUtil.parse(s, DEFAULT_REGION_CODE);
                return phoneNumberUtil.isValidNumber(number);
            } catch (NumberParseException e) {
                return false;
            }
        }
    }
}
