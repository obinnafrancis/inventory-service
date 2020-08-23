package com.vlad.tech.inventoryservice.services;

import com.vlad.tech.inventoryservice.daos.PermissionRepository;
import com.vlad.tech.inventoryservice.daos.UserRepository;
import com.vlad.tech.inventoryservice.models.dtos.Permission;
import com.vlad.tech.inventoryservice.models.dtos.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AuthenticateService implements UserDetailsService {
    @Autowired
    UserRepository userDao;
    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User exists = userDao.findByUsername(username);
        if (!Objects.nonNull(exists)){
            throw new UsernameNotFoundException("Invalid username");
        }
        return buildUser(exists);
    }

    private UserDetails buildUser(User exists) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Permission permission:exists.getRole().getPermissionList()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+permission.getName()));
        }
        return new org.springframework.security.core.userdetails.User(exists.getUsername(),exists.getPassword(),authorities);
    }
}
