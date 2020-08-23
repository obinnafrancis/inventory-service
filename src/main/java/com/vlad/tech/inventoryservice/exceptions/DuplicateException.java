package com.vlad.tech.inventoryservice.exceptions;

import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class DuplicateException extends ApplicationException {
    public DuplicateException(ResponseMapper userAlreadyExists) {
        super(userAlreadyExists);
    }
}
