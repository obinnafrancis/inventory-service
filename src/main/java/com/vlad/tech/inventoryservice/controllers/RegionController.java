package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Region;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.requests.UpdateRegionRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.RegionService;
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
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping
    @Secured("ROLE_CREATE_REGION")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> createRegion (@RequestBody @Valid Region region, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST, Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = regionService.createRegion(region);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_REGION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getRegion(@PathVariable (value = "id") String identifier) {
        return () -> {
            if(Utils.isDigit(identifier)){
                BaseResponse response = regionService.findRegion(Long.parseLong(identifier));
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
    @Secured("ROLE_GET_ALL_REGIONS")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllRegions() {
        return () -> {
                BaseResponse response = regionService.findAllRegions();
                return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_REGION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateRegion (@PathVariable(value = "id") long id, @RequestBody UpdateRegionRequest updateRegionRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            Region region = new Region();
            Utils.copyNonNullProperties(updateRegionRequest,region);
            region.setId(id);
            regionService.updateRegion(region);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,region), HttpStatus.OK);
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_REGION")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteRegion (@PathVariable(value = "id") String identifier){
        return () ->{
            if(Utils.isDigit(identifier)){
                regionService.deleteRegionById(Long.parseLong(identifier));
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
