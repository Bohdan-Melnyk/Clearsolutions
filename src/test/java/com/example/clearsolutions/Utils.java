package com.example.clearsolutions;

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
}
