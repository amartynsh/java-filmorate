package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserStorage {
    User addUser(@Valid User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    void deleteUserById(long id);
}
