package ru.yandex.practicum.filmorate.service;

import jakarta.validation.constraints.NotNull;
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
        validateUser(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void deleteUserById(Long id) {
        userStorage.deleteUserById(id);
    }

    public void addFriend(long userId, Long newFriendToAddId) {
        log.info("Пользователь {} добавляет друга {}", userId, newFriendToAddId);
        User user = userStorage.getUserById(userId);
        User newFriendToAdd = userStorage.getUserById(newFriendToAddId);

        if (newFriendToAdd == null) {
            throw new NotFoundException("Добавляемого друга не существует");
        }
        if (user == null) {
            throw new NotFoundException("Пользователя не существует");
        }
        user.getFriends().add(newFriendToAddId);
        log.info("Пользователь {} добавил друга {}, теперь список друзей {}", userId, newFriendToAdd,
                user.getFriends());
        newFriendToAdd.getFriends().add(userId);
        log.info("Добавили другу {} пользователя в друзья {}, теперь список друзей {}", newFriendToAdd, userId,
                newFriendToAdd.getFriends());
    }

    public void removeFriend(long userId, long friendToRemoveId) {
        userStorage.getUserById(userId).getFriends().remove(friendToRemoveId);
        userStorage.getUserById(friendToRemoveId).getFriends().remove(userId);
    }

    public List<User> getFriends(long userId) {
        return userStorage.getUserById(userId).getFriends().stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<User> compareFriendLists(@NotNull long userId, @NotNull long friendToCompareId) {

        User user = userStorage.getUserById(userId);
        User userToCompare = userStorage.getUserById(friendToCompareId);
        List<Long> listOfFriends = new ArrayList<>(user.getFriends());
        List<Long> result = new ArrayList<>(userToCompare.getFriends());
        result.retainAll(listOfFriends);

        return result.stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validateUser(User user) {

        log.trace("Начало процесса валидации пользователя");

        if (user.getLogin().contains(" ")) {
            log.info("Валидация login не пройдена, есть пробелы в  Login = {} ", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробел");
        }
    }
}