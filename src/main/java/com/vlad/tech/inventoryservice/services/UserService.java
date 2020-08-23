package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.LocationRepository;
import com.vlad.tech.inventoryservice.daos.RoleRepository;
import com.vlad.tech.inventoryservice.daos.UserRepository;
import com.vlad.tech.inventoryservice.exceptions.*;
import com.vlad.tech.inventoryservice.models.dtos.Location;
import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.Role;
import com.vlad.tech.inventoryservice.models.dtos.User;
import com.vlad.tech.inventoryservice.models.requests.LoginRequest;
import com.vlad.tech.inventoryservice.models.requests.RegisterUserRequest;
import com.vlad.tech.inventoryservice.models.requests.UpdateUserRequest;
import com.vlad.tech.inventoryservice.models.responses.AuthenticatedUser;
import com.vlad.tech.inventoryservice.models.responses.AuthenticationResponse;
import com.vlad.tech.inventoryservice.models.responses.BaseResponse;
import com.vlad.tech.inventoryservice.security.AuthenticationFacade;
import com.vlad.tech.inventoryservice.utils.JwtUtil;
import com.vlad.tech.inventoryservice.utils.ResponseMapper;
import com.vlad.tech.inventoryservice.utils.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private LocationRepository locationRepository;
    private UserRepository userDao;
    private AuthenticationFacade authenticationFacade;

    public UserService(){}

    @Autowired
    public UserService(UserRepository userDao, JwtUtil jwtUtil, RoleRepository roleRepository, LocationRepository locationRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade){
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.authenticationFacade = authenticationFacade;
        this.locationRepository = locationRepository;
    }

    @SneakyThrows
    public BaseResponse RegisterUser(RegisterUserRequest registerUserRequest) {
        //check existence
        User exists = userDao.findByUsername(registerUserRequest.getEmail());
        if(Objects.nonNull(exists)){
            throw new DuplicateException(ResponseMapper.USER_ALREADY_EXISTS);
        }else {
            User user = User.builder().build();
            BeanUtils.copyProperties(user,registerUserRequest);
            user.setUsername(registerUserRequest.getEmail());
            user.setPhoneNumber(Utils.normalizeMobileNo(registerUserRequest.getPhoneNumber()));
            Role role = roleRepository.findById(registerUserRequest.getRoleId()).orElse(Role.builder().name("GUEST").description("Guest User.").build());
            user.setRole(role);
            user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
            user.setUserTag(Utils.generateUUID());
            user.setDateCreated(new Date());
            user.setDateModified(new Date());
            userDao.save(user);
            user.setPassword(null);
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,user);
        }
    }

    public BaseResponse authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
            User exists = userDao.findByUsername(loginRequest.getUsername());
            AuthenticatedUser user = new AuthenticatedUser();
            BeanUtils.copyProperties(user,exists);
            Map<String,Object> userObj = Utils.returmMapOfClass(user);
            String token = jwtUtil.customCreateToken(userObj,loginRequest.getUsername());
            Date exp = jwtUtil.extractExpiration(token);
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, new AuthenticationResponse(token,exp));
        }catch (BadCredentialsException | IllegalAccessException| InvocationTargetException e){
            log.error(e.getMessage());
            throw new CustomAccessDeniedException(ResponseMapper.INVALID_USERNAME_PASSWORD);
        }
    }

    public BaseResponse deleteUser(String identifier) {
        long id;
        try {
            id = Long.parseLong(identifier);
        } catch (Exception ex){
            log.error(ex.getMessage());
            throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
        }
        User exists = userDao.findById(id).get();
        if(Objects.nonNull(exists)){
            userDao.delete(exists);
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE,null);
        }else {
            throw new NotFoundException(ResponseMapper.NO_REGION_FOUND);
        }
    }

    public BaseResponse getMe() {
        Authentication authentication = authenticationFacade.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userDao.findByUsername(userDetails.getUsername());
        if(Objects.nonNull(user)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, user);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse fetchUser(String identifier){
        long id;
        try {
            id = Long.parseLong(identifier);
        }catch (Exception e){
            throw new InvalidParamereException(ResponseMapper.BAD_REQUEST);
        }
        User fetch = userDao.findById(id).get();
        if(Objects.nonNull(fetch)){
            return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, fetch);
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
    }

    public BaseResponse updateUser(UpdateUserRequest updateUserRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User exists = userDao.findByUsername(updateUserRequest.getUsername());
        boolean checker = false;
        if (Objects.nonNull(exists)){
            if (userDetails.getUsername().equalsIgnoreCase(updateUserRequest.getUsername())){
                update(updateUserRequest, exists);
            }else {
                User checkAdmin = userDao.findByUsername(userDetails.getUsername());
                if(checkAdmin.getRole().getName().toUpperCase().contains("ADMIN")){
                   update(updateUserRequest, exists);
                } else {
                    throw new ForbiddenAccessException(ResponseMapper.FORBIDDEN_ACCESS);
                }
            }
        }else {
            throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
        }
        return new BaseResponse(ResponseMapper.SUCCESS_RESPONSE, null);
    }

    private void update(UpdateUserRequest updateUserRequest, User exists){

        if (Objects.nonNull(exists)){
            if(updateUserRequest.getRoleId()>0){
                Optional<Role> exRole = roleRepository.findById(updateUserRequest.getRoleId());
                if(!exRole.isPresent()){
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
                exists.setRole(exRole.get());
            }
            if(updateUserRequest.getLocationId()>0){
                Optional<Location> exLocation = locationRepository.findById(updateUserRequest.getLocationId());
                if(!exLocation.isPresent()){
                    throw new NotFoundException(ResponseMapper.NO_RECORD_FOUND);
                }
                exists.setLocation(exLocation.get());
            }
            Utils.copyNonNullProperties(updateUserRequest,exists);
            userDao.save(exists);
        }
    }
}
