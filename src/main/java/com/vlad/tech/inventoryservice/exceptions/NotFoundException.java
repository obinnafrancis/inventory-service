package com.vlad.tech.inventoryservice.exceptions;

import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String code, String message){
        super(code,message);
    }
    public NotFoundException(ResponseMapper noRecordFound) {
        super(noRecordFound);
    }
}
