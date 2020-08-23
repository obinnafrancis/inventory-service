package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Oem;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.requests.UpdateOemRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.OemService;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/oem")
public class OemController {
    @Autowired
    private OemService oemService;

    @PostMapping
    @Secured("ROLE_CREATE_OEM")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> createOem (@RequestBody @Valid Oem oem, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST, Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = oemService.createOem(oem);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_OEM")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getOem(@PathVariable (value = "id") String identifier) {
        return () -> {
            if(Utils.isDigit(identifier)){
                BaseResponse response = oemService.findOem(Long.parseLong(identifier));
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else {
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {identifier}")
                        .description("Invalid.format")
                        .build();
                List<ValidationError> errors = new ArrayList<>();
                errors.add(validationError);
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,errors), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @GetMapping
    @Secured("ROLE_GET_ALL_OEMS")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllOems() {
        return () -> {
                BaseResponse response = oemService.findAllOem();
                return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_OEM")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateOem (@PathVariable(value = "id") long id, @RequestBody UpdateOemRequest updateOemRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            Oem oem = new Oem();
            Utils.copyNonNullProperties(updateOemRequest,oem);
            oem.setId(id);
            oemService.updateOem(oem);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,oem), HttpStatus.OK);
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_OEM")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteOem (@PathVariable(value = "id") String identifier){
        return () ->{
            if(Utils.isDigit(identifier)){
                oemService.deleteOemById(Long.parseLong(identifier));
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,null), HttpStatus.OK);
            }else {
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {identifier}")
                        .description("Invalid.format")
                        .build();
                List<ValidationError> errors = new ArrayList<>();
                errors.add(validationError);
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,errors), HttpStatus.BAD_REQUEST);
            }
        };
    }
}
