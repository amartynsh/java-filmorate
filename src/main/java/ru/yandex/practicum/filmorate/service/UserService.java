package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (userStorage.getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException("Обновляемого пользователя несуществует");
        }
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        if (userStorage.getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getUserById(id).get();
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }

    public void addFriend(long userId, Long newFriendToAddId) {
        log.info("Пользователь {} добавляет друга {}", userId, newFriendToAddId);

        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователя с ID: " + userId + " нет");
        }
        if (userStorage.getUserById(newFriendToAddId).isEmpty()) {
            throw new NotFoundException("Добавляемого друга с ID: " + newFriendToAddId + " нет");
        }

        User user = userStorage.getUserById(userId).get();
        User newFriendToAdd = userStorage.getUserById(newFriendToAddId).get();

        user.getFriends().add(newFriendToAddId);
        log.info("Пользователь {} добавил друга {}, теперь список друзей {}", userId, newFriendToAdd,
                user.getFriends());
        newFriendToAdd.getFriends().add(userId);
        log.info("Добавили другу {} пользователя в друзья {}, теперь список друзей {}", newFriendToAdd, userId,
                newFriendToAdd.getFriends());
    }

    public void removeFriend(long userId, long friendToRemoveId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Удаляемый пользователь не найден");
        }
        if (userStorage.getUserById(friendToRemoveId).isEmpty()) {
            throw new NotFoundException("Удаляемый друг не найден");
        }
        userStorage.getUserById(userId).get().getFriends().remove(friendToRemoveId);
        userStorage.getUserById(friendToRemoveId).get().getFriends().remove(userId);
    }

    public List<User> getFriends(long userId) {

        return getUserById(userId).getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> compareFriendLists(long userId, long friendToCompareId) {
        User user = getUserById(userId);
        User userToCompare = getUserById(friendToCompareId);

        List<Long> listOfFriends = new ArrayList<>(user.getFriends());
        List<Long> result = new ArrayList<>(userToCompare.getFriends());
        result.retainAll(listOfFriends);

        return result.stream()
                .map(this::getUserById)
                .toList();
    }

    private void validateUser(User user) {

        log.trace("Начало процесса валидации пользователя");

        if (user.getLogin().contains(" ")) {
            log.info("Валидация login не пройдена, есть пробелы в  Login = {} ", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробел");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.trace("Заменили имя пользователя {} на логин {}", user.getName(), user.getLogin());
        }
    }
}