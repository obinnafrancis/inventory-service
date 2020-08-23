package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Inventory;
import com.vlad.tech.inventoryservice.models.dtos.Project;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.requests.UpdateInventoryRequest;
import com.vlad.tech.inventoryservice.models.requests.UpdateProjectRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.InventoryService;
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
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    @Secured("ROLE_CREATE_INVENTORY")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create (@RequestBody @Valid Inventory inventory, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = inventoryService.createInventory(inventory);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_INVENTORY")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getInventory(@PathVariable (value = "id") String identifier) {
        return () -> {
            if(Utils.isDigit(identifier)){
                BaseResponse response = inventoryService.findInventory(Long.parseLong(identifier));
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
    @Secured("ROLE_GET_ALL_INVENTORIES")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllInventories() {
        return () -> {
            BaseResponse response = inventoryService.findAllInventories();
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_INVENTORY")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateInventory (@PathVariable(value = "id") long id, @RequestBody UpdateInventoryRequest updateInventoryRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            Inventory inventory = new Inventory();
            Utils.copyNonNullProperties(updateInventoryRequest,inventory);
            inventory.setId(id);
            inventoryService.updateInventory(inventory);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,inventory), HttpStatus.OK);
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_INVENTORY")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteInventory (@PathVariable(value = "id") String identifier){
        return () ->{
            if(Utils.isDigit(identifier)){
                inventoryService.deleteInventoryById(Long.parseLong(identifier));
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
