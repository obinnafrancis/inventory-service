package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.InventoryRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.InvalidParamereException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Inventory;
import com.vlad.tech.inventoryservice.models.dtos.Product;
import com.vlad.tech.inventoryservice.models.dtos.Project;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProjectService projectService;
    private final ProductService productService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, ProductService productService, ProjectService projectService){
        this.inventoryRepository = inventoryRepository;
        this.projectService = projectService;
        this.productService = productService;
    }

    public Inventory find(long id) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(id);
        if(inventoryOptional.isPresent()){
            return inventoryOptional.get();
        }
        return null;
    }

    public BaseResponse createInventory(Inventory inventory) {
        Inventory exists = inventoryRepository.findByTagName(inventory.getTagName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.INVENTORY_ALREADY_EXISTS);
        }else {
            if(Objects.nonNull(inventory.getProduct())) {
                if (Objects.isNull(productService.find(inventory.getProduct().getId()))) {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
            }else {
                throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
            }
            if(Objects.nonNull(inventory.getProject())) {
                if (Objects.isNull(projectService.find(inventory.getProject().getId()))) {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
            }else {
                throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
            }
            inventory.setDateCreated(new Date());
            inventory.setDateModified(new Date());
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,inventoryRepository.save(inventory));
        }
    }

    public BaseResponse findInventory(long id) {
       Inventory exists = find(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateInventory(Inventory inventory) {
        Optional<Inventory> optionalExists = inventoryRepository.findById(inventory.getId());
        if(optionalExists.isPresent()){
            Inventory exists = optionalExists.get();
            if(Objects.nonNull(inventory.getProduct())) {
                Product product = productService.find(inventory.getProduct().getId());
                if (Objects.isNull(product)) {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
                inventory.setProduct(product);
            }else {
                throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
            }
            if(Objects.nonNull(inventory.getProject())) {
                Project project = projectService.find(inventory.getProject().getId());
                if (Objects.isNull(project)) {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
                inventory.setProject(project);
            }else {
                throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
            }
            Utils.copyNonNullProperties(inventory, exists);
            inventoryRepository.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteInventoryById(long id) {
        Optional<Inventory> exists = inventoryRepository.findById(id);
        if(exists.isPresent()){
            inventoryRepository.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllInventories() {
        List<Inventory> inventoryList =  this.inventoryRepository.findAll();
        if(inventoryList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, inventoryList);
    }
}
