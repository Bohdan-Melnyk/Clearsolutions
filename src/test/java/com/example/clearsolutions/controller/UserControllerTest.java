package com.example.clearsolutions.controller;

import com.example.clearsolutions.Utils;
import com.example.clearsolutions.dto.ResponseDto;
import com.example.clearsolutions.entity.User;
import com.example.clearsolutions.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }


    @Test
    void createUserTest() throws Exception {
        var john = getUser();
        Mockito.doNothing().when(userService).createUser(john);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(john)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        new ResponseDto(String.valueOf(HttpStatus.CREATED.value()),
                                "User created successfully"))));
    }

    @Test
    void updateUserTest() throws Exception {
        String email = "test@email.com";
        var john = getUser();
        Mockito.doNothing().when(userService).updateUser(email, john);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(john))
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        new ResponseDto(String.valueOf(HttpStatus.OK.value()),
                                "User updated successfully"))));
    }

    @Test
    void updateOptionalUserFieldsTest() throws Exception {
        String email = "test@email.com";
        String address = "456 Elm Avenue";
        String phoneNumber = "987-654-3210";
        var optionalFieldsDto = Utils.optionalFieldsDto(address, phoneNumber);
        Mockito.doNothing().when(userService).updateOptionalUserFields(email, optionalFieldsDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(optionalFieldsDto))
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        new ResponseDto(String.valueOf(HttpStatus.OK.value()),
                                "User updated successfully"))));
    }

    @Test
    void deleteUser() throws Exception {
        String email = "test@email.com";
        Mockito.doNothing().when(userService).deleteUser(email);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(
                        new ResponseDto(String.valueOf(HttpStatus.OK.value()),
                                "User deleted successfully"))));
    }

    @Test
    void userSearch() throws Exception {
        LocalDate from = (LocalDate.of(1997, 1, 1));
        LocalDate to = LocalDate.of(1999, 12, 31);
        var sarah = Utils.createUser("sarah@email.com",
                "Sarah",
                "Brown",
                LocalDate.of(1997, 12, 22));
        userService.createUser(sarah);
        var ryan = Utils.createUser("ryan@email.com",
                "Ryan",
                "Scott",
                LocalDate.of(1998, 9, 11));
        userService.createUser(ryan);
        var christopher = Utils.createUser("christopher@email.com",
                "Christopher",
                "Murphy",
                LocalDate.of(1999, 1, 24));
        userService.createUser(christopher);

        Mockito.when(userService.birthRange(from, to)).thenReturn(List.of(sarah, ryan, christopher));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("from", String.valueOf(from))
                        .param("to", String.valueOf(to)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private User getUser() {
        var john = Utils.createUser("example1@email.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 2));
        return john;
    }
}
