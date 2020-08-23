package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.OemRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Oem;
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
public class OemService {
    private final OemRepository oemRepository;

    @Autowired
    public OemService(OemRepository oemRepository){
        this.oemRepository = oemRepository;
    }

    public Oem find(long id) {
        Optional<Oem> oemOptional = oemRepository.findById(id);
        if(oemOptional.isPresent()){
            return oemOptional.get();
        }
        return null;
    }

    public BaseResponse createOem(Oem oem) {
        Oem exists = oemRepository.findByOemName(oem.getOemName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.OEM_ALREADY_EXISTS);
        }else {
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,oemRepository.save(oem));
        }
    }

    public BaseResponse findOem(long id) {
       Oem exists = find(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateOem(Oem oem) {
        Optional<Oem> optionalExists = oemRepository.findById(oem.getId());
        if(optionalExists.isPresent()){
            Oem exists = optionalExists.get();
            Utils.copyNonNullProperties(oem, exists);
            oemRepository.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteOemById(long id) {
        Optional<Oem> exists = oemRepository.findById(id);
        if(exists.isPresent()){
            oemRepository.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllOem() {
        List<Oem> oemList =  this.oemRepository.findAll();
        if(oemList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, oemList);
    }
}
