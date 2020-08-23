package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.LocationRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Location;
import com.vlad.tech.inventoryservice.models.dtos.Region;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class LocationService {
    private final LocationRepository locationDao;
    private final RegionService regionService;
    @Autowired
    public LocationService (LocationRepository locationDao, RegionService regionService){
        this.locationDao = locationDao;
        this.regionService = regionService;
    }

    public BaseResponse createLocation(Location location) {
        Region exists = regionService.find(location.getRegion().getId());
        if(Objects.nonNull(exists)){
            Location loc = locationDao.findByLocationTagAndRegionId(location.getLocationTag(),location.getRegion().getId());
            if(Objects.nonNull(loc)){
                throw new DuplicateException(ResponseMapper.LOCATION_ALREADY_EXISTS);
            }
            location.setRegion(exists);
            location.setDateCreated(new Date());
            location.setDateModified(new Date());
            location = locationDao.save(location);
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,location);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public Location findById(long id){
        return locationDao.findById(id).get();
    }

    public Location findByLocationTag(String tag){
        return locationDao.findByLocationTag(tag);
    }

    public void updateLocation(Location location){
        Location exists = findById(location.getId());
        if(Objects.nonNull(exists)){
            if(location.getRegion().getId()>0){
                Region ex = regionService.find(location.getRegion().getId());
                if(!Objects.nonNull(ex)){
                    throw new NotFoundException(ResponseMapper.NO_REGION_FOUND);
                }
                location.setRegion(ex);
            }
            Utils.copyNonNullProperties(location, exists);
            locationDao.save(location);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteLocationById(long id){
        Location exists = locationDao.findById(id).get();
        if(Objects.nonNull(exists)){
            locationDao.delete(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse findLocation(long id) {
        Location exists = findById(id);
        if(Objects.nonNull(exists)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse getAllLocations() {
        List<Location> locationList =  this.locationDao.findAll();
        if(locationList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, locationList);
    }
}
