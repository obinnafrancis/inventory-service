package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.Role;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.RoleService;
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
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @PostMapping
    @Secured("ROLE_CREATE_ROLE")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create(@RequestBody @Valid Role role, Errors errors){
        return ()->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            return roleService.createRole(role);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_ROLE")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getRole (@PathVariable (value = "id") String id){
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
            return roleService.getRole(id);
        };
    }

    @GetMapping
    @Secured("ROLE_GET_ALL_ROLES")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllRoles (){
        return ()->{
            return roleService.getAllRoles();
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_ROLE")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Callable<ResponseEntity<BaseResponse>> updateRole (@RequestBody @Valid Role role, @PathVariable (value = "id") String id, Errors errors){
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
                role.setId(Long.parseLong(id));
                roleService.updateRole(role);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,validationErrors), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_ROLE")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Callable<ResponseEntity<BaseResponse>> deleteRole (@PathVariable (value = "id") String id){
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
            roleService.deleteRole(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        };
    }

    @PostMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_MAP_ROLE_PERMISSIONS")
    public Callable<ResponseEntity<BaseResponse>> mapPermissionsToRole(@RequestBody List<Permission> permissionList, @PathVariable(value = "id") String roleId){
        return ()->{
            List<ValidationError> validationErrors = new ArrayList<>();
            if(!Utils.isDigit(roleId)) {
                ValidationError validationError = ValidationError.builder()
                        .fieldName("path variable {id}")
                        .description("Invalid.format")
                        .build();
                validationErrors.add(validationError);
            }
            if(permissionList.isEmpty() || permissionList.size()<1){
                ValidationError validationError = ValidationError.builder()
                        .fieldName("request body permissions list")
                        .description("list.cannot.be.empty")
                        .build();
                validationErrors.add(validationError);
            }
            if(validationErrors.isEmpty()){
                return roleService.assignPermissionsToRole(Long.parseLong(roleId),permissionList);
            }else {
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST, validationErrors), HttpStatus.BAD_REQUEST);
            }
        };
    }
}
