package com.example.clearsolutions.service.impl;

import com.example.clearsolutions.dto.UserOptionalFieldsDto;
import com.example.clearsolutions.entity.User;
import com.example.clearsolutions.exception.UserAlreadyExistException;
import com.example.clearsolutions.exception.UserNotAdultException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Map<String, User> userMap = new HashMap<>();

    @Value("${user.age}")
    private int age;

    /**
     *
     Creates a new user.
     @param newUser the User object containing information about the user to be created
     */
    @Override
    public void createUser(User newUser) {
        checkIfUserAlreadyExist(newUser.getEmail());
        isEighteenYearsOld(newUser.getBirthDate());
        userMap.put(newUser.getEmail(), newUser);
    }

    /**
     *
     Updates all fields of an existing user with the specified email address.
     @param email the email address of the user to be updated
     @param user the User object containing updated information
     */
    @Override
    public void updateUser(String email, User user) {
        checkIfUserFound(email);
        isEighteenYearsOld(user.getBirthDate());
        userMap.put(user.getEmail(), user);
    }

    /**
     *
     Updates optional fields (address and phone number) of an existing user with the specified email address.
     @param email the email address of the user whose optional fields are to be updated
     @param dto the UserOptionalFieldsDto object containing the optional fields to update
     */
    @Override
    public void updateOptionalUserFields(String email, UserOptionalFieldsDto dto) {
        checkIfUserFound(email);
        var userToUpdate = getUserByEmail(email);
        userToUpdate.setAddress(dto.getAddress());
        userToUpdate.setPhoneNumber(dto.getPhoneNumber());
    }

    /**
     *
     Deletes a user with the specified email address.
     @param email the email address of the user to be deleted
     */
    @Override
    public void deleteUser(String email) {
        checkIfUserFound(email);
        userMap.remove(email);
    }

    /**
     *
     Returns the size of the users map.
     @return the size of the users map
     */
    @Override
    public int getUsersMapSize() {
        return userMap.size();
    }

    /**
     *
     Retrieves a list of users born within the specified date range.
     @param from the start date of the birth range (inclusive)
     @param to the end date of the birth range (inclusive)
     If from greater than to returns empty list
     @return a list of User objects born within the specified date range
     */
    @Override
    public List<User> birthRange(LocalDate from, LocalDate to) {
        if (from.isBefore(to)) {
            return userMap.values()
                    .stream()
                    .filter(user -> !user.getBirthDate().isBefore(from) && !user.getBirthDate().isAfter(to))
                    .toList();
        }
        return Collections.emptyList();
    }

    /**
     *
     Checks if a user with the specified email address is present.
     @param email the email address of the user to check
     @return true if a user with the specified email address is present, false otherwise
     */
    @Override
    public boolean isUserExist(String email) {
        return userMap.containsKey(email);
    }

    /**
     *
     Retrieves the user with the specified email address.
     @param email the email address of the user to retrieve
     @return the User object corresponding to the specified email address, or null if not found
     */
    @Override
    public User getUserByEmail(String email) {
        checkIfUserFound(email);
        return userMap.get(email);
    }

    private void isEighteenYearsOld(LocalDate dateOfBirth) {
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < age) {
            throw new UserNotAdultException("User is not" + age + " years old");
        }
    }

    private void checkIfUserAlreadyExist(String email) {
        if (isUserExist(email)) {
            throw new UserAlreadyExistException("User with email " + email + " already exists");
        }
    }

    private void checkIfUserFound(String email) {
        if (!isUserExist(email)) {
            throw new UserNotFoundException("User with email " + email + " doesn't exist");
        }
    }
}
