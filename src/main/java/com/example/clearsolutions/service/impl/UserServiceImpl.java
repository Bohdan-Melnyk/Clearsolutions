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

    private static Map<String, User> userMap = new HashMap<>();

    @Value("${user.age}")
    private int age;

    @Override
    public void createUser(User newUser) {
        checkIfUserAlreadyExist(newUser.getEmail());
        isEighteenYearsOld(newUser.getBirthDate());
        userMap.put(newUser.getEmail(), newUser);
    }

    @Override
    public void updateUser(User user) {
        checkIfUserFound(user.getEmail());
        isEighteenYearsOld(user.getBirthDate());
        userMap.put(user.getEmail(), user);
    }

    @Override
    public void updateOptionalUserFields(String email, UserOptionalFieldsDto dto) {
        checkIfUserFound(email);
        userMap.get(email).setAddress(dto.getAddress());
        userMap.get(email).setPhoneNumber(dto.getPhoneNumber());
    }

    @Override
    public void deleteUser(String email) {
        checkIfUserFound(email);
        userMap.remove(email);
    }

    @Override
    public int getUsersMapSize() {
        return userMap.size();
    }

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

    private void isEighteenYearsOld(LocalDate dateOfBirth) {
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < age) {
            throw new UserNotAdultException("User is not 18 years old");
        }
    }

    private void checkIfUserAlreadyExist(String email) {
        if (userMap.containsKey(email)) {
            throw new UserAlreadyExistException("User with email " + email + " already exists");
        }
    }

    private void checkIfUserFound(String email) {
        if (!userMap.containsKey(email)) {
            throw new UserNotFoundException("User with email " + email + " doesn't exist");
        }
    }
}
