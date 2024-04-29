package com.example.clearsolutions.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class User {

    @Email(message = "Wrong email format")
    private String email;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String LastName;

    @Past(message = "Date must be in past")
    private LocalDate birthDate;

    @Nullable
    private String address;

    @Nullable
    private String phoneNumber;
}
