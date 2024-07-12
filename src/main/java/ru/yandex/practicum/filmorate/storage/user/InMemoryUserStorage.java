package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    public static boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}" +
                        "~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(@Valid User user) {
        log.info("Обращение к endpoint POST //user");
        log.info("Пользователь из запроса POST: {}", user);
        log.info("Текущий список пользователей: {}", users);
        validateUser(user);
        log.info("Валидация пользователя {} прошла", user);
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        user.setFriends(new HashSet<>());
        log.info("Установлен ID пользователя {}", user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь сохранен {}", user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.info("Обращение к endpoint PUT /user");
        if (!users.containsKey(user.getId()) || user.getId() == 0) {
            throw new NotFoundException("Такого пользователя нет");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
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

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {

        log.trace("Начало процесса валидации пользователя");
        if ((user.getEmail() == null || user.getEmail().isEmpty())) {
            log.info("Валидацию email не прошла, ошибочное значение: {} ", user.getEmail());
            throw new ValidationException("Электронная почта не указана");
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User getUserById(long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        return users.get(id);
    }

    public void deleteUserById(long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        users.remove(id);
    }
}