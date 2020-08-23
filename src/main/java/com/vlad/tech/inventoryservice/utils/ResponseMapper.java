package com.vlad.tech.inventoryservice.utils;

public enum ResponseMapper {

    SUCCESS_RESPONSE ("00","Successful"),
    FAILED_RESPONSE ("01","Failed"),
    BAD_REQUEST ("40000","Field Validation Error"),
    NO_RECORD_FOUND ("40400","no.record.found"),
    NO_REGION_FOUND ("40400","no.record.found.for.reqion"),
    USER_ALREADY_EXISTS ("40900","User with username already exists"),
    ROLE_ALREADY_EXISTS ("40900","Role with name already exists"),
    REGION_ALREADY_EXISTS ("40900","Region already exists"),
    OEM_ALREADY_EXISTS ("40900","OEM already exists"),
    INVENTORY_ALREADY_EXISTS ("40900","Inventory already exists"),
    PROJECT_ALREADY_EXISTS ("40900","Storage already exists"),
    LOCATION_ALREADY_EXISTS ("40900","Location already exists"),
    PRODUCT_ALREADY_EXISTS ("40900","Product already exists"),
    STORAGE_ALREADY_EXISTS ("40900","Storage already exists"),
    PERMISSION_ALREADY_EXISTS ("40900","Permission with name already exists"),
    INVALID_MOBILE  ("40000","Invalid Mobile Number"),
    INVALID_USERNAME_PASSWORD("40100","Invalid Username/Password"),
    FORBIDDEN_ACCESS("40300","Sorry you do not have access to this resource"),
    SERVER_ERROR    ("50000", "Internal Server Error");

    private String code;
    private String description;

    ResponseMapper(String code, String description){
        this.code = code;
        this.description = description;
    }

    public static String getResponseDescription(String code){
        for(ResponseMapper responseMapper: ResponseMapper.values()){
            if(responseMapper.getCode().equals(code)){
                return responseMapper.getDescription();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
