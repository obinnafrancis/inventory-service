package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.requests.LoginRequest;
import com.vlad.tech.inventoryservice.models.requests.RegisterUserRequest;
import com.vlad.tech.inventoryservice.models.requests.UpdateUserRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.UserService;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> authenticateUser (@RequestBody @Valid LoginRequest loginRequest, Errors errors){
        return ()->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = userService.authenticate(loginRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @GetMapping("/me")
    @Secured("ROLE_GET_SESSION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> userMe (){
        return () ->{
            return new ResponseEntity<>(userService.getMe(), HttpStatus.OK);
        };
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create (@RequestBody @Valid RegisterUserRequest registerUserRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = userService.RegisterUser(registerUserRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping
    @Secured("ROLE_GET_USER")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getUsers (@RequestParam (required = false, value = "identifier") String identifier){
        return () ->{
            return new ResponseEntity<>(userService.fetchUser(identifier), HttpStatus.OK);
        };
    }

    @GetMapping("/hello")
    @Secured("ROLE_UPDATE_ROLE")
    @ResponseStatus(HttpStatus.OK)
    public String hello (){
        return "HttpStatus.OK";
    }

    @PutMapping
    @Secured("ROLE_UPDATE_USER")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateUser (@RequestBody @Valid UpdateUserRequest updateUserRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(userService.updateUser(updateUserRequest), HttpStatus.OK);
        };
    }

    @DeleteMapping
    @Secured("ROLE_DELETE_USER")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteUser (@RequestParam(required = true, value = "identifier") String identifier){
        return () ->{
            return new ResponseEntity<>(userService.deleteUser(identifier), HttpStatus.OK);
        };
    }
}
