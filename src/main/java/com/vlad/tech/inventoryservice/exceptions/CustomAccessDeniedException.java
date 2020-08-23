package com.vlad.tech.inventoryservice.exceptions;

import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class CustomAccessDeniedException extends ApplicationException {
    public CustomAccessDeniedException(String message){
        super(message);
    }

    public CustomAccessDeniedException(ResponseMapper message){
        super(message);
    }
}
