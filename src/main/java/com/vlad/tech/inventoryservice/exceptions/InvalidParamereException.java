package com.vlad.tech.inventoryservice.exceptions;

import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class InvalidParamereException extends ApplicationException {
    public InvalidParamereException(ResponseMapper error) {
        super(error);
    }

    public InvalidParamereException (String message, ResponseMapper error) {
        super(message,error);
    }
}
