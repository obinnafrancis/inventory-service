package com.vlad.tech.inventoryservice.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vlad.tech.inventoryservice.models.dtos.Gender;
import com.vlad.tech.inventoryservice.utils.validators.EnumValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {
    private String username;
    private String firstname;
    private String middlename;
    private String lastname;
    private String userType;
    private long roleId;
    private long locationId;
    @EnumValidator(message = "{gender.mismatch}", enumClass = Gender.class)
    private String gender;
}
