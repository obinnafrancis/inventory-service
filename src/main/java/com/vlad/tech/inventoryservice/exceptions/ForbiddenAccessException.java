package com.vlad.tech.inventoryservice.exceptions;


import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class ForbiddenAccessException extends ApplicationException {
    public ForbiddenAccessException(String code, String description) {
        super(code,description);
    }
    public ForbiddenAccessException(ResponseMapper error) {
        super(error);
    }

    public ForbiddenAccessException(String message, ResponseMapper error) {
        super(message,error);
    }
}
