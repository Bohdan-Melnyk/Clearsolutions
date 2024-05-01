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

    private static final String WRONG_EMAIL_FORMAT = "Wrong email format";


    @PostMapping("/users")
    public ResponseEntity<ResponseDto> createUser(@RequestBody @Valid User user) {
        userService.createUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(String.valueOf(HttpStatus.CREATED.value()), "User created successfully"));
    }

    @PutMapping("/users")
    public ResponseEntity<ResponseDto> updateUser(@RequestParam @Email(message = WRONG_EMAIL_FORMAT) String email,
                                                  @RequestBody @Valid User user) {
        userService.updateUser(email, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(String.valueOf(HttpStatus.OK.value()), "User updated successfully"));
    }

    @PatchMapping("/users")
    public ResponseEntity<ResponseDto> updateOptionalUserFields(@RequestParam @Email(message = WRONG_EMAIL_FORMAT) String email,
                                                                @RequestBody @Valid UserOptionalFieldsDto dto) {
        userService.updateOptionalUserFields(email, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(String.valueOf(HttpStatus.OK.value()), "User updated successfully"));
    }

    @DeleteMapping("/users")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam @Email(message = WRONG_EMAIL_FORMAT) String email) {
        userService.deleteUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(String.valueOf(HttpStatus.OK.value()), "User deleted successfully"));
    }

    @PostMapping("/users/search")
    public ResponseEntity<List<User>> userSearch(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.birthRange(from, to));
    }
}
