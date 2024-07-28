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
    public Optional<User> addUser(User user) {
        log.info("Начали сохранять пользователя {}", user);
        validateName(user);
        user.setId(getNextId());
        log.trace("Установлен ID пользователя {}", user.getId());
        users.put(user.getId(), user);
        log.trace("Пользователь сохранен {}", user);
        return getUserById(user.getId());
    }

    @Override
    public void updateUser(User user) {
        log.info("Начали обновлять пользователя {}", user.getId());
        validateName(user);
        users.put(user.getId(), user);
        log.trace("Записали пользователя {}", users.get(user.getId()));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получаем список пользователей");
        return new ArrayList<>(users.values());
    }

    private long getNextId() {
        return filmId++;
    }

    @Override
    public Optional<User> getUserById(long id) {
        log.info("Начали искать пользователя {}", id);
        return Optional.ofNullable(users.get(id));
    }

    public void deleteUserById(long id) {
        log.info("Начали удалять пользователя {}", id);
        if (users.remove(id) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    @Override
    public void addFriend(long userId, long friendId) {

    }

    @Override
    public void deleteFriend(long userId, long friendId) {

    }

    @Override
    public List<User> getFriends(long userId) {
        return List.of();
    }

    private void validateName(User user) {
        if (user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}