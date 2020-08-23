package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.ProductRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Oem;
import com.vlad.tech.inventoryservice.models.dtos.Product;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {
    private final OemService oemService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, OemService oemService){
        this.productRepository = productRepository;
        this.oemService = oemService;
    }

    public Product find(long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()){
            return productOptional.get();
        }
        return null;
    }

    public BaseResponse createProduct(Product product) {
        Product exists = productRepository.findByName(product.getName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.PRODUCT_ALREADY_EXISTS);
        }else {
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,productRepository.save(product));
        }
    }

    public BaseResponse findProduct(long id) {
       Product exists = find(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateProduct(Product product) {
        Product exists = find(product.getId());
        if(Objects.nonNull(exists)){
            if(Objects.nonNull(product.getOem())){
                Oem oemExists = oemService.find(product.getOem().getId());
                if(Objects.nonNull(oemExists)){
                    product.setOem(oemExists);
                }else {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
            }
            Utils.copyNonNullProperties(product, exists);
            productRepository.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteProductById(long id) {
        Optional<Product> exists = productRepository.findById(id);
        if(exists.isPresent()){
            productRepository.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllProducts() {
        List<Product> productList =  this.productRepository.findAll();
        if(productList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, productList);
    }
}
