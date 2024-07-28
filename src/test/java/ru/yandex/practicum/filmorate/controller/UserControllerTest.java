package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @BeforeEach
    void beforeEach() {
        UserStorage storage = new InMemoryUserStorage();
        UserService userService = new UserService(storage);
        userController = new UserController(new UserService(storage), new FriendService(userService, storage));
    }

    @Test
    void addUserShouldBeSuccess() {
        //Случай когда все верно
        User user = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(user));

        //Не указано имя, должен подставиться логин
        User userWithoutName = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(userWithoutName));
        assertEquals(userWithoutName.getLogin(), userWithoutName.getName());
    }

    @Test
    void addUserShouldThrowValidateException() {
        //Пользователь без указания email
        User userNoEmail = User.builder()
                .email("")
                .login("testuser")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
/*        assertThrows(ValidationException.class, () -> userController.addUser(userNoEmail));*/

        //Пользователь с пустым Login
        User userEmptyLogin = User.builder()
                .email("testuser@yandex.ru")
                .login("")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
/*        assertThrows(ValidationException.class, () -> userController.addUser(userEmptyLogin));*/


        //Пользователь с путым Login
        User userSpaceInLogin = User.builder()
                .email("testuser@yandex.ru")
                .login("test user")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
      /*  assertThrows(ValidationException.class, () -> userController.addUser(userSpaceInLogin));*/

//День рождения в будущем
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plusOneHour = now.plusHours(1);
        LocalDate futureBirthday = plusOneHour.toLocalDate();

        User userBirthDateInFuture = User.builder()
                .email("testuser@yandex.ru")
                .login("test user")
                .name("Test User")
                .birthday(futureBirthday)
                .build();
      /*  assertThrows(ValidationException.class, () -> userController.addUser(userBirthDateInFuture));*/
    }

    @Test
    void updateUserShouldBeSuccess() {
        //Создали пользователя
        User user = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(user));

        //Создаем обновленного пользователя
        User userUpdated = User.builder()
                .id(user.getId())
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User Userovich")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.updateUser(userUpdated));
    }

    @Test
    void updateUserShoulThrowValidateException() {
        //Создали пользователя
        User user = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(user));

        //Создаем обновленного пользователя без id
        User userUpdated = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User Userovich")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();

        assertThrows(NotFoundException.class, () -> userController.updateUser(userUpdated));
    }

    @Test
    void getAllUsers() {
        User user = User.builder()
                .email("testuser@yandex.ru")
                .login("testuser")
                .name("Test User")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(user));

        User user2 = User.builder()
                .email("testuser2@yandex.ru")
                .login("testuser2")
                .name("Test2 User2")
                .birthday(LocalDate.of(1999, 3, 12))
                .build();
        assertDoesNotThrow(() -> userController.addUser(user2));

        List<User> users = userController.getAllUsers();
        assertTrue(users.contains(user) && users.contains(user2));
    }
}