package com.vlad.tech.inventoryservice.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUser {
    private String username;
    private String firstname;
    private String lastname;
    private String middlename;
    private String phoneNumber;
    private String email;
    private String roleName;
    private String gender;
    private String userTag;
}
