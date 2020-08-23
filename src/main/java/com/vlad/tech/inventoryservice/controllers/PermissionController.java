package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.PermissionService;
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
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @PostMapping
    @Secured("ROLE_CREATE_PERMISSION")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create(@RequestBody @Valid Permission permission, Errors errors){
        return ()->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            return permissionService.createPermission(permission);
        };
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_GET_PERMISSION")
    public Callable<ResponseEntity<BaseResponse>> getPermission (@PathVariable (value = "id") String id){
        return ()->{
            if(!Utils.isDigit(id)){
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {id}")
                        .description("Invalid.format")
                        .build();
                List<ValidationError> errors = new ArrayList<>();
                errors.add(validationError);
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,errors), HttpStatus.BAD_REQUEST);
            }
            return permissionService.getPermission(id);
        };
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_GET_ALL_PERMISSIONS")
    public Callable<ResponseEntity<BaseResponse>> getAllPermissions (){
        return ()->{
            return permissionService.getAllPermissions();
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_PERMISSION")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Callable<ResponseEntity<BaseResponse>> updatePermissions (@RequestBody @Valid Permission permission, @PathVariable (value = "id") String id, Errors errors){
        return ()->{
            List<ValidationError> validationErrors = new ArrayList<>();
            if(Utils.getFieldErrors(errors)!=null){
                validationErrors.addAll(Utils.getFieldErrors(errors));
            }
            if(!Utils.isDigit(id)){
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {id}")
                        .description("Invalid.format")
                        .build();
                validationErrors.add(validationError);
            }
            if(validationErrors.isEmpty()){
                permission.setId(Long.parseLong(id));
                permissionService.updatePermission(permission);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,validationErrors), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_PERMISSION")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Callable<ResponseEntity<BaseResponse>> deletePermission (@PathVariable (value = "id") String id){
        return ()->{
            if(!Utils.isDigit(id)) {
                List<ValidationError> validationErrors = new ArrayList<>();
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {id}")
                        .description("Invalid.format")
                        .build();
                validationErrors.add(validationError);
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST, validationErrors), HttpStatus.BAD_REQUEST);
            }
            permissionService.deletePermission(Integer.parseInt(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        };
    }
}
