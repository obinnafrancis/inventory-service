package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Location;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.requests.UpdateLocationRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.LocationService;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @PostMapping
    @Secured("ROLE_CREATE_LOCATION")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create (@RequestBody @Valid Location location, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = locationService.createLocation(location);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_LOCATION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getLocation(@PathVariable (required = false, value = "id") String identifier) {
        return () -> {
            if(Utils.isDigit(identifier)){
                BaseResponse response = locationService.findLocation(Long.parseLong(identifier));
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
    @Secured("ROLE_GET_ALL_LOCATIONS")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllLocations (){
        return ()->{
            return new ResponseEntity<>(locationService.getAllLocations(), HttpStatus.OK);
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_LOCATION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateLocation (@PathVariable(value = "id") long id, @RequestBody @Valid UpdateLocationRequest updateLocationRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            Location location = new Location();
            Utils.copyNonNullProperties(updateLocationRequest,location);
            location.setId(id);
            locationService.updateLocation(location);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,location), HttpStatus.OK);
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_LOCATION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteLocation (@PathVariable(value = "id") String identifier){
        return () ->{
            if(Utils.isDigit(identifier)){
                locationService.deleteLocationById(Integer.parseInt(identifier));
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
