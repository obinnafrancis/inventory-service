package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.StorageRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Location;
import com.vlad.tech.inventoryservice.models.dtos.Storage;
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
public class StorageService {
    private final StorageRepository storageRepository;
    private final LocationService locationService;

    @Autowired
    public StorageService(StorageRepository storageRepository, LocationService locationService){
        this.storageRepository = storageRepository;
        this.locationService = locationService;
    }

    public Storage find(long id) {
        Optional<Storage> oemOptional = storageRepository.findById(id);
        if(oemOptional.isPresent()){
            return oemOptional.get();
        }
        return null;
    }

    public BaseResponse createStorage(Storage storage) {
        Storage exists = storageRepository.findByName(storage.getName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.STORAGE_ALREADY_EXISTS);
        }else {
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,storageRepository.save(storage));
        }
    }

    public BaseResponse findStorage(long id) {
       Storage exists = find(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateStorage(Storage storage) {
        Optional<Storage> optionalExists = storageRepository.findById(storage.getId());
        if(optionalExists.isPresent()){
            Storage exists = optionalExists.get();
            if(storage.getLocation().getId()>0){
                Location locExists = locationService.findById(storage.getLocation().getId());
                if(Objects.nonNull(locExists)){
                    storage.setLocation(locExists);
                }else {
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
            }else {
                storage.setLocation(null);
            }
            Utils.copyNonNullProperties(storage, exists);
            storageRepository.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteStorageById(long id) {
        Optional<Storage> exists = storageRepository.findById(id);
        if(exists.isPresent()){
            storageRepository.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllStorages() {
        List<Storage> storageList =  this.storageRepository.findAll();
        if(storageList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, storageList);
    }
}
