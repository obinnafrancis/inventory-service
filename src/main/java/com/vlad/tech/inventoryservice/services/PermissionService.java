package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.PermissionRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PermissionService {
    private PermissionRepository permissionDao;
    @Autowired
    public PermissionService (PermissionRepository permissionDao){ this.permissionDao = permissionDao; }

    public ResponseEntity<BaseResponse> createPermission(Permission permission) {
        Permission exists = permissionDao.findByName(permission.getName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.PERMISSION_ALREADY_EXISTS);
        }
        String description = StringUtils.hasText(permission.getDescription()) ? permission.getDescription() : permission.getName()+" Permission";
        permission.setDescription(description);
        create(permission);
        return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, permission), HttpStatus.CREATED);
    }

    private Permission create(Permission permission) {
        return this.permissionDao.save(permission);
    }

    public ResponseEntity<BaseResponse> getPermission(String search) {
        long id = Long.parseLong(search);
        Permission exists = find(id);
        if(Objects.nonNull(exists)){
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,exists),HttpStatus.OK);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public Permission find(long id) {
        Optional<Permission> optionalPermission = this.permissionDao.findById(id);
        if(optionalPermission.isPresent()){
            return optionalPermission.get();
        }
        return null;
    }

    public ResponseEntity<BaseResponse> getAllPermissions() {
        List<Permission> permissionList = findAll();
        if(permissionList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }else {
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,permissionList),HttpStatus.OK);
        }
    }

    private List<Permission> findAll() {
        return this.permissionDao.findAll();
    }

//    public List<Permission> getPermissionsByRoleId(long id) {
//        return this.permissionDao.getPermissionsByRoleId(id);
//    }

    public void updatePermission(Permission permission) {
        Permission exists = find(permission.getId());
        if(Objects.nonNull(exists)){
            Utils.copyNonNullProperties(permission, exists);
            this.permissionDao.save(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deletePermission(int id) {
        Permission exists = find(id);
        if(Objects.nonNull(exists)){
            this.permissionDao.delete(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }
}
