package com.vlad.tech.inventoryservice.controllers.advice;

import com.vlad.tech.inventoryservice.exceptions.*;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = {
        "com.vlad.tech.inventoryservice.utils",
        "com.vlad.tech.inventoryservice.config",
        "com.vlad.tech.inventoryservice.controllers",
        "com.vlad.tech.inventoryservice"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<BaseResponse> handleExceptionAdvice(NotFoundException e){
        log.error(e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ResponseEntity<BaseResponse> handleExceptionAdvice(DuplicateException e){
        log.error(e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidParamereException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleExceptionAdvice(InvalidParamereException e){
        log.error(e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<BaseResponse> handleExceptionAdvice(CustomAccessDeniedException e){
        log.error(e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseEntity<BaseResponse> handleExceptionAdvice(ForbiddenAccessException e){
        log.error(e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setCode(e.getErrorCode());
        response.setDescription(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }
}
