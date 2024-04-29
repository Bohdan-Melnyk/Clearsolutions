package com.example.clearsolutions.controller;

import com.example.clearsolutions.dto.ResponseDto;
import com.example.clearsolutions.dto.UserOptionalFieldsDto;
import com.example.clearsolutions.entity.User;
import com.example.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/user/create")
    public ResponseEntity<ResponseDto> createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto("201", "User created successfully"));
    }

    @PutMapping("/user/update")
    public ResponseEntity<ResponseDto> updateUser(@RequestBody @Valid User user) {
        userService.updateUser(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", "User updated successfully"));
    }

    @PatchMapping("/user/update/optional")
    public ResponseEntity<ResponseDto> updateOptionalUserFields(@RequestParam @Email String email,
                                                                @RequestBody @Valid UserOptionalFieldsDto dto) {
        userService.updateOptionalUserFields(email, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", "User updated successfully"));
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam @Email String email) {
        userService.deleteUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto("200", "User deleted successfully"));
    }

    @PostMapping("/user/search")
    public ResponseEntity<List<User>> userSearch(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.birthRange(from, to));
    }
}
