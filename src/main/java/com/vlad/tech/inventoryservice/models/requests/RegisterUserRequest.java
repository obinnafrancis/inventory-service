package com.vlad.tech.inventoryservice.models.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vlad.tech.inventoryservice.models.dtos.Gender;
import com.vlad.tech.inventoryservice.utils.validators.EnumValidator;
import com.vlad.tech.inventoryservice.utils.validators.FieldMatch;
import com.vlad.tech.inventoryservice.utils.validators.PhoneValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldMatch(first = "password", second = "confirmPassword", message = "{password.mismatch}")
public class RegisterUserRequest{
    @NotBlank
    private String firstname;
    private String middlename;
    @NotBlank
    private String lastname;
    @NotBlank
//    @PasswordPolicy(message = "{password.policy.violation}")
    private String password;
    @NotBlank
    private String confirmPassword;
    @NotBlank
    @EnumValidator(message = "{gender.mismatch}", enumClass = Gender.class)
    private String gender;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @PhoneValidator
    private String phoneNumber;
    private long roleId;
}
