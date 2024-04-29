package com.example.clearsolutions;

import com.example.clearsolutions.exception.UserAlreadyExistException;
import com.example.clearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "age", 18);
    }

    @Test
    void createUserSuccessTest() {
        var john = Utils.createUser("example@email.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 2));
        userService.createUser(john);
        Assertions.assertEquals(1, userService.getUsersMapSize());
    }

    @Test
    void createUserUserAlreadyExistExceptionTest() {
        var john = Utils.createUser("example1@email.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 2));
        var vitaliy = Utils.createUser("example1@email.com",
                "Vitaliy",
                "Baton",
                LocalDate.of(2000, 3, 15));
        userService.createUser(john);

        Exception exception = Assertions.assertThrows(UserAlreadyExistException.class,
                () -> userService.createUser(vitaliy));
    }

}
