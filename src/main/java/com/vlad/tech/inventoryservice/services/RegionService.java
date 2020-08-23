package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.RegionRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Region;
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
public class RegionService {
    private final RegionRepository regionDao;

    @Autowired
    public RegionService(RegionRepository regionDao){
        this.regionDao = regionDao;
    }

    public Region find(long id) {
        Optional<Region> regionOptional = regionDao.findById(id);
        if(regionOptional.isPresent()){
            return regionOptional.get();
        }
        return null;
    }

    public BaseResponse createRegion(Region region) {
        Region exists = regionDao.findByRegionAndCountry(region.getRegion(), region.getCountry());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.REGION_ALREADY_EXISTS);
        }else {
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,regionDao.save(region));
        }
    }

    public BaseResponse findRegion(long id) {
        Optional<Region> region = regionDao.findById(id);
        if(region.isPresent()){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, region.get());
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void updateRegion(Region region) {
        Optional<Region> optionalExists = regionDao.findById(region.getId());
        if(optionalExists.isPresent()){
            Region exists = optionalExists.get();
            Utils.copyNonNullProperties(region, exists);
            regionDao.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteRegionById(long id) {
        Optional<Region> exists = regionDao.findById(id);
        if(exists.isPresent()){
            regionDao.delete(exists.get());
        }else{
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findAllRegions() {
        List<Region> regionList =  this.regionDao.findAll();
        if(regionList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, regionList);
    }
}
