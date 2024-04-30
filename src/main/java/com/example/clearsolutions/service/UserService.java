package com.example.clearsolutions.service;

import com.example.clearsolutions.dto.UserOptionalFieldsDto;
import com.example.clearsolutions.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {

    void createUser(User user);

    void updateUser(String email, User user);

    void updateOptionalUserFields(String email, UserOptionalFieldsDto dto);

    void deleteUser(String email);

    int getUsersMapSize();

    List<User> birthRange(LocalDate from, LocalDate to);

    Map<String, User> userMap();
}
