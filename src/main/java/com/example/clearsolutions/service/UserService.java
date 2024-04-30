package com.example.clearsolutions.service;

import com.example.clearsolutions.dto.UserOptionalFieldsDto;
import com.example.clearsolutions.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    /**
     *
     Creates a new user.
     @param user the User object containing information about the user to be created
     */
    void createUser(User user);

    /**
     *
     Updates all fields of an existing user with the specified email address.
     @param email the email address of the user to be updated
     @param user the User object containing updated information
     */
    void updateUser(String email, User user);

    /**
     *
     Updates optional fields (address and phone number) of an existing user with the specified email address.
     @param email the email address of the user whose optional fields are to be updated
     @param dto the UserOptionalFieldsDto object containing the optional fields to update
     */
    void updateOptionalUserFields(String email, UserOptionalFieldsDto dto);

    /**
     *
     Deletes a user with the specified email address.
     @param email the email address of the user to be deleted
     */
    void deleteUser(String email);

    /**
     *
     Returns the size of the users map.
     @return the size of the users map
     */
    int getUsersMapSize();

    /**
     *
     Retrieves a list of users born within the specified date range.
     @param from the start date of the birth range (inclusive)
     @param to the end date of the birth range (inclusive)
     If from greater than to returns empty list
     @return a list of User objects born within the specified date range
     */
    List<User> birthRange(LocalDate from, LocalDate to);


    /**
     *
     Checks if a user with the specified email address is present.
     @param email the email address of the user to check
     @return true if a user with the specified email address is present, false otherwise
     */
    boolean isUserPresent(String email);


    /**
     *
     Retrieves the user with the specified email address.
     @param email the email address of the user to retrieve
     @return the User object corresponding to the specified email address, or null if not found
     */
    User readUser(String email);
}
