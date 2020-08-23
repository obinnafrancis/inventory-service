package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.RoleRepository;
import com.vlad.tech.inventoryservice.exceptions.DuplicateException;
import com.vlad.tech.inventoryservice.exceptions.NotFoundException;
import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.Role;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class RoleService {

    private RoleRepository roleDao;
    private PermissionService permissionService;

    @Autowired
    public RoleService(RoleRepository roleDao, PermissionService permissionService){
        this.roleDao = roleDao;
        this.permissionService = permissionService;
    }

    public ResponseEntity<BaseResponse> createRole(Role role) {
        Role exists = roleDao.findByName(role.getName());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.ROLE_ALREADY_EXISTS);
        }
        String description = StringUtils.hasText(role.getDescription()) ? role.getDescription() : role.getName()+" Role";
        role.setDescription(description);
        create(role);
        return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, role), HttpStatus.CREATED);
    }

    private Role create(Role role) {
        return roleDao.save(role);
    }

    private Role find(long id){
        Optional<Role> optionalRole = roleDao.findById(id);
        if(optionalRole.isPresent()){
            return optionalRole.get();
        }
        return null;
    }

    private List<Role> findAll(){
        return roleDao.findAll();
    }

    public ResponseEntity<BaseResponse> getRole(String search) {
        int id = Integer.parseInt(search);
        Role exists = find(id);
        if(Objects.nonNull(exists)){
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,exists),HttpStatus.OK);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public ResponseEntity<BaseResponse> getAllRoles() {
        List<Role> roleList = findAll();
        if(roleList.isEmpty()){
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }else {
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,roleList),HttpStatus.OK);
        }
    }

    public void updateRole(Role role) {
        Role exists = find(role.getId());
        if (Objects.nonNull(exists)){
            Utils.copyNonNullProperties(role, exists);
            roleDao.save(role);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public void deleteRole(long id) {
        Role exists = find(id);
        if (Objects.nonNull(exists)){
            roleDao.delete(exists);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public ResponseEntity<BaseResponse> assignPermissionsToRole(int id, List<Permission> permissionList) {
        Map<String,Object> reportMap = new HashMap<>();
        List<Long> assigned = new ArrayList<>();
        List<Long> failed = new ArrayList<>();
        Role exists = find(id);
        if (Objects.nonNull(exists)){
            for(Permission permission:permissionList){
                Permission permissionExists = permissionService.find(permission.getId());
                if(Objects.nonNull(permissionExists)){
                    mapPermissionToRole(id,permission.getId());
                    assigned.add(permission.getId());
                }else {
                    failed.add(permission.getId());
                    continue;
                }
            }
            reportMap.put("Successful Mapped Permissions : " , assigned);
            reportMap.put("Failed Mapped Permissions : " , failed);
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,reportMap),HttpStatus.CREATED);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public ResponseEntity<BaseResponse> assignPermissionsToRole(long roleId, List<Permission> permissionList) {
        Role role = roleDao.findById(roleId).get();
        if(Objects.nonNull(role)){
            role.setPermissionList(new HashSet<>(permissionList));
            return new ResponseEntity<>(new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,roleDao.save(role)),HttpStatus.CREATED);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    private void mapPermissionToRole(long roleId, long permissionId) {
//        this.roleDao.assignPermissionToRole(roleId,permissionId);
    }
}
