package com.vlad.tech.inventoryservice.exceptions;

import com.vlad.tech.inventoryservice.utils.ResponseMapper;

public class ApplicationException extends RuntimeException{
    String code;
    protected ResponseMapper error = ResponseMapper.SERVER_ERROR;
    public ApplicationException(){
        super();
    }

    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable cause){
        super(message,cause);
    }

    public ApplicationException(ResponseMapper error){
        super(error.getDescription());
        this.code = error.getCode();
        this.error = error;
    }

    public ApplicationException(String code,String message){
        super(message);
        this.code = code;
    }

    public ApplicationException(String message, ResponseMapper error){
        super(message);
        this.code = error.getCode();
        this.error = error;
    }

    public ApplicationException(String message, Throwable cause, ResponseMapper error){
        super(message,cause);
        this.error = error;
    }

    public String getErrorCode(){
        return error.getCode();
    }
}
