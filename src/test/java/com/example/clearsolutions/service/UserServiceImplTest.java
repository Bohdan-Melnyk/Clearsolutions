package com.example.clearsolutions.service;

import com.example.clearsolutions.Utils;
import com.example.clearsolutions.exception.UserAlreadyExistException;
import com.example.clearsolutions.exception.UserNotAdultException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        var benjamin = Utils.createUser("benjamin@email.com",
                "Benjamin",
                "Anderson",
                LocalDate.of(86, 11, 10));
        userService.createUser(benjamin);
        assertTrue(userService.isUserExist("example@email.com"));
        assertTrue(userService.isUserExist("benjamin@email.com"));
    }

    @Test
    void createUserUserAlreadyExistExceptionTest() {
        var john = Utils.createUser("example1@email.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 2));
        var david = Utils.createUser("example1@email.com",
                "David",
                "Jones",
                LocalDate.of(2000, 3, 15));
        userService.createUser(john);

        assertThrows(UserAlreadyExistException.class,
                () -> userService.createUser(david));
    }

    @Test
    void createUserUserNotAdultExceptionTest() {
        var olivia = Utils.createUser("example@email.com",
                "Olivia",
                "Taylor",
                LocalDate.of(2015, 7, 12));
        assertThrows(UserNotAdultException.class,() -> userService.createUser(olivia));
    }

    @Test
    void updateUserSuccessTest() {
        String newEmail = "emilyJohnson@email.com";
        var emily = Utils.createUser(newEmail,
                "Emily",
                "Johnson",
                LocalDate.of(2006, 4, 8));
        userService.updateUser("example@email.com", emily);
        var updatedUser = userService.getUserByEmail(newEmail);

        assertEquals(emily.getBirthDate(), updatedUser.getBirthDate());
        assertEquals(emily.getFirstName(), updatedUser.getFirstName());
        assertEquals(emily.getLastName(), updatedUser.getLastName());
    }

    @Test
    void updateUserUserNotFoundException() {
        var ethan = Utils.createUser("ethan@email.com",
                "Ethan",
                "Parker",
                LocalDate.of(2006, 4, 8));
        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser("notExist@email.com", ethan));
    }

    @Test
    void updateOptionalUserFieldsSuccessTest() {
        String email = "miller@email.com";
        var alexander = Utils.createUser(email,
                "Alexander",
                "Miller",
                LocalDate.of(2002, 4, 29));
        userService.createUser(alexander);

        String address = "456 Elm Avenue";
        String phoneNumber = "987-654-3210";

        var optionalFieldsDto = Utils.optionalFieldsDto(address, phoneNumber);
        userService.updateOptionalUserFields(email, optionalFieldsDto);
        var updatedUser = userService.getUserByEmail(email);

        assertEquals(address, updatedUser.getAddress());
        assertEquals(phoneNumber, updatedUser.getPhoneNumber());
    }

    @Test
    void deleteUserSuccessTest() {
        String michaelEmail = "michael@email.com";
        String jessicaEmail = "jessica@email.com";
        var jessica = Utils.createUser(jessicaEmail,
                "Jessica",
                "Wilson",
                LocalDate.of(1995, 10, 12));
        userService.createUser(jessica);
        var michael = Utils.createUser(michaelEmail,
                "Michael",
                "Smith",
                LocalDate.of(1991, 5, 19));
        userService.createUser(michael);

        userService.deleteUser(jessicaEmail);
        userService.deleteUser(michaelEmail);

        assertFalse(userService.isUserExist(jessicaEmail));
        assertFalse(userService.isUserExist(michaelEmail));
    }

    @Test
    void birthRangeTest() {
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

        var users = userService.birthRange(LocalDate.of(1997, 1, 1),
                LocalDate.of(1999, 12, 31));

        assertEquals(3, userService.getUsersMapSize());
    }

    @Test
    void birthRangeTestEmptyTest() {
        int size = userService.birthRange(LocalDate.of(2020, 3, 14),
                LocalDate.of(2006, 6, 7)).size();

        assertEquals(0, size);
    }
}
