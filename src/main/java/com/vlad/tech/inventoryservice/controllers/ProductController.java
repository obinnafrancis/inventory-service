package com.vlad.tech.inventoryservice.controllers;

import com.vlad.tech.inventoryservice.models.dtos.Product;
import com.vlad.tech.inventoryservice.models.dtos.ValidationError;
import com.vlad.tech.inventoryservice.models.requests.UpdateProductRequest;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.services.ProductService;
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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @Secured("ROLE_CREATE_PRODUCT")
    @ResponseStatus(HttpStatus.CREATED)
    public Callable<ResponseEntity<BaseResponse>> create (@RequestBody @Valid Product product, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            BaseResponse response = productService.createProduct(product);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        };
    }

    @GetMapping("/{id}")
    @Secured("ROLE_GET_PRODUCT")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getProduct(@PathVariable (value = "id") String identifier) {
        return () -> {
            if(Utils.isDigit(identifier)){
                BaseResponse response = productService.findProduct(Long.parseLong(identifier));
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
    @Secured("ROLE_GET_ALL_PRODUCTS")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> getAllProducts() {
        return () -> {
            BaseResponse response = productService.findAllProducts();
            return new ResponseEntity<>(response, HttpStatus.OK);
        };
    }

    @PutMapping("/{id}")
    @Secured("ROLE_UPDATE_PRODUCT")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> updateProduct (@PathVariable(value = "id") long id, @RequestBody UpdateProductRequest updateProductRequest, Errors errors){
        return () ->{
            if(Utils.getFieldErrors(errors)!=null){
                return new ResponseEntity<>(new BaseResponse(ResponseMapper.BAD_REQUEST,Utils.getFieldErrors(errors)), HttpStatus.BAD_REQUEST);
            }
            Product product = new Product();
            Utils.copyNonNullProperties(updateProductRequest,product);
            product.setId(id);
            productService.updateProduct(product);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,product), HttpStatus.OK);
        };
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_DELETE_PRODUCT")
    @ResponseStatus(HttpStatus.OK)
    public Callable<ResponseEntity<BaseResponse>> deleteProduct (@PathVariable(value = "id") String identifier){
        return () ->{
            if(Utils.isDigit(identifier)){
                productService.deleteProductById(Long.parseLong(identifier));
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
