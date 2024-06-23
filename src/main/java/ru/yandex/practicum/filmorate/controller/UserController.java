package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Обращение к endpoint POST //user");
        log.info("Пользователь из запроса POST: {}", user);
        log.info("Текущий список пользователей: {}", users);
        validateUser(user);
        log.info("Валидация пользователя {} прошла", user);
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.info("Установлен ID пользователя {}", user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь сохранен {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обращение к endpoint PUT /user");
        if (!users.containsKey(user.getId()) || user.getId() == 0) {
            throw new ValidationException("Такого пользователя нет");
        }
        validateUser(user);
        log.info("Прошла валидация PUT /user");
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.trace("Проверили имя {} и логин {}", user.getName(), user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Записали пользователя {}", users.get(user.getId()));
        return user;

    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {

        log.trace("Начало процесса валидации пользователя");
        if ((user.getEmail() == null || user.getEmail().isEmpty())) {
            log.info("Валидацию email не прошла, ошибочное значение: {} ", user.getEmail());
            throw new ValidationException("Электронная почта не указана");
        }
        if (!user.getEmail().contains("@")) {
            log.info("Валидацию email не прошла, email не содержит знак @: {} ", user.getEmail());
            throw new ValidationException("Неверный формат электронной почты");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.info("Валидация login не пройдена, пустое значение Login = {} ", user.getLogin());
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.info("Валидация login не пройдена, есть пробелы в  Login = {} ", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробел");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Валидация даты рождения не пройдена, дата не может быть в будущем birthDay = {} ", user.getBirthday());
            throw new ValidationException("Дата рождения должна быть в прошлом!");
        }
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}