package com.vlad.tech.inventoryservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vlad.tech.inventoryservice.exceptions.InvalidParamereException;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.lang.reflect.InvocationTargetException;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    private static final String REGION_CODE_SEPERATOR = "|";
    private static final String DEFAULT_REGION_CODE = "NG";
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public static List<ValidationError> getFieldErrors (Errors errors){
        if (errors.hasFieldErrors()){
            List<FieldError> errorList = errors.getFieldErrors();
            List<ValidationError> issuesList = new ArrayList<>();
            for (FieldError error: errorList){
                ValidationError validationError = ValidationError.builder()
                        .fieldName(error.getField())
                        .description(error.getDefaultMessage())
                        .build();
                issuesList.add(validationError);
            }
            return issuesList;
        }
        return null;
    }

    public static boolean isNullOrEmpty(String s) {
        if(s==null || s.trim()==""){
            return true;
        }else {
            return false;
        }
    }

    public static String normalizeMobileNo(String mobile) {
        try {
            String[] values = mobile.split(String.format("\\%s",REGION_CODE_SEPERATOR));
            String country;
            String number;
            switch (values.length){
                case 1:
                    country = DEFAULT_REGION_CODE;
                    number = values[0];
                    break;
                case 2:
                    country = values[0];
                    number = values[1];
                    break;
                default:
                    throw new InvalidParamereException(ResponseMapper.INVALID_MOBILE);
            }
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            PhoneNumberUtil.PhoneNumberType type = phoneNumberUtil.getNumberType(phoneNumber);
            if(type == PhoneNumberUtil.PhoneNumberType.MOBILE ||
                    type == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE){
                String phoneNumberE164 = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                return phoneNumberE164.startsWith("+") ? phoneNumberE164.substring(1) : phoneNumberE164;
            } else {
                throw new InvalidParamereException(ResponseMapper.INVALID_MOBILE);
            }
        }catch (NumberParseException e){
            throw new InvalidParamereException(ResponseMapper.INVALID_MOBILE);
        }
    }

    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("\\d+?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static Map<String,Object> returmMapOfClass(Object object){
        ObjectMapper mapObject = new ObjectMapper();
        Map < String, Object > mapObj = mapObject.convertValue(object, Map.class);
        return mapObj;
    }

    public static String generateUUID(){
        long timestamp = System.nanoTime();
        int random = 10 + new SecureRandom(SecureRandom.getSeed(2)).nextInt(9*10);
        return "User_"+timestamp + random;
    }

    public static void copyNonNullProperties(Object source, Object destination){
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set emptyNames = new HashSet();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return (String[]) emptyNames.toArray(result);
    }
}
