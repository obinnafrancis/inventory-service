package com.vlad.tech.inventoryservice.models.dtos;

import org.springframework.security.core.GrantedAuthority;

public class Grants implements GrantedAuthority {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
