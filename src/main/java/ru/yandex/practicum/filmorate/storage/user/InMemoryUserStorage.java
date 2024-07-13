package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long filmId = 1;

    @Override
    public User addUser(User user) {
        log.info("Начали сохранять пользователя {}", user);
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.info("Установлен ID пользователя {}", user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь сохранен {}", user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.info("Начали обновлять пользователя {}", user.getId());
        if (!users.containsKey(user.getId()) || user.getId() == 0) {
            throw new NotFoundException("Такого пользователя нет");
        }

        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.trace("Заменили имя пользователя {} на логин {}", user.getName(), user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Записали пользователя {}", users.get(user.getId()));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private long getNextId() {
        return filmId++;
    }

    @Override
    public User getUserById(long id) {
        return Optional.of(users.get(id)).orElseThrow(() -> new NotFoundException("Такого пользователя нет"));
    }

    public void deleteUserById(long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        users.remove(id);
    }
}