package com.example.clearsolutions;

import com.example.clearsolutions.dto.UserOptionalFieldsDto;
import com.example.clearsolutions.entity.User;

import java.time.LocalDate;

public class Utils {

    public static User createUser(String email, String firstName, String lastname, LocalDate dateOfBirth) {
        var user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastname);
        user.setBirthDate(dateOfBirth);
        return user;
    }

    public static UserOptionalFieldsDto optionalFieldsDto(String address, String phoneNumber) {
        var optionalFieldsDto = new UserOptionalFieldsDto();
        optionalFieldsDto.setAddress(address);
        optionalFieldsDto.setPhoneNumber(phoneNumber);
        return optionalFieldsDto;
    }
}
