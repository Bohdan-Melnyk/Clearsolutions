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
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static Map<String, User> userMap = new HashMap<>();

    @Value("${user.age}")
    private int age;

    @Override
    public void createUser(User newUser) {
        userExistCheck(newUser);

        isEighteenYearsOld(newUser.getBirthDate());

        userMap.put(newUser.getEmail(), newUser);
    }

    @Override
    public void updateUser(User user) {
        var currUser = getUser(user.getEmail());
        if (currUser.isPresent()) {
            userMap.put(currUser.get().getEmail(), user);
        } else throw new UserNotFoundException("User with email " + user.getEmail() + " doesn't exist");

    }

    @Override
    public void updateOptionalUserFields(String email, UserOptionalFieldsDto dto) {
        var user = getUser(email);
        if (user.isPresent()) {
            user.get().setAddress(dto.getAddress());
            user.get().setPhoneNumber(dto.getPhoneNumber());
        } else throw new UserNotFoundException("User with email " + email + " doesn't exist");
    }

    @Override
    public void deleteUser(String email) {
        if (userMap.containsKey(email)) {
            userMap.remove(email);
        } else throw new UserNotFoundException("User with email " + email + " doesn't exist");
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

    private Optional<User> getUser(String email) {
        return Optional.ofNullable(userMap.get(email));
    }

    private void isEighteenYearsOld(LocalDate dateOfBirth) {
        if (Period.between(dateOfBirth, LocalDate.now()).getYears() < age) {
            throw new UserNotAdultException("User is not 18 years old");
        }
//        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= age;
    }

    private void userExistCheck(User user) {
        if (userMap.containsKey(user.getEmail())) {
            throw new UserAlreadyExistException("User with email " + user.getEmail() + " already exists");
        }
    }
}
