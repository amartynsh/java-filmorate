package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FriendService {

    UserStorage userStorage;
    UserService userService;

    public FriendService(UserService userService, @Qualifier("userDbStorage") UserStorage userStorage) {

        this.userService = userService;
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, Long newFriendToAddId) {
        log.info("Пользователь {} добавляет друга {}", userId, newFriendToAddId);

        userService.getUserById(userId);// проверка на существование пользователя
        userService.getUserById(newFriendToAddId);

        User user = userService.getUserById(userId);
        User newFriendToAdd = userService.getUserById(newFriendToAddId);

        userStorage.addFriend(userId, newFriendToAddId);

        log.info("Пользователь {} добавил друга {}, теперь список друзей {}", userId, newFriendToAdd,
                user.getFriends());

        log.info("Добавили другу {} пользователя в друзья {}, теперь список друзей {}", newFriendToAdd, userId,
                newFriendToAdd.getFriends());
    }

    public void removeFriend(long userId, long friendToRemoveId) {
        userService.getUserById(userId);// проверка на существование пользователя
        userService.getUserById(friendToRemoveId);

        userStorage.deleteFriend(userId, friendToRemoveId);
    }

    public List<User> getFriends(long id) {
        userService.getUserById(id);
        return userStorage.getFriends(id);
    }

    public List<User> compareFriendLists(long userId, long friendToCompareId) {
        userService.getUserById(userId);
        userService.getUserById(friendToCompareId);

        List<User> listOfFriends = new ArrayList<>(getFriends(userId));
        List<User> result = new ArrayList<>(getFriends(friendToCompareId));
        result.retainAll(listOfFriends);

        return result;
    }
}