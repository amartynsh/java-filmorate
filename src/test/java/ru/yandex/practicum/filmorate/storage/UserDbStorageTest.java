package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/testconf/schema.sql", "/testconf/fortest1.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testconf/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UserDbStorageTest {

    @Autowired
    public UserDbStorage userDbStorage;
    @Autowired
    public FriendService friendService;

    @Test
    public void addNewUserTest() {
        assertThat(userDbStorage.getAllUsers().size()).isEqualTo(5);
        User user = new User(1L, "test@test.ru", "test", "test", LocalDate.now());
        userDbStorage.addUser(user);
        assertThat(userDbStorage.getAllUsers().size()).isEqualTo(6);
    }

    @Test
    public void getUserByIdTest() {
        assertThat(userDbStorage.getUserById(1)).get().hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void updateUserTest() {
        assertThat(userDbStorage.getUserById(1L)).get().hasFieldOrPropertyWithValue("name", "user1");
        if (userDbStorage.getUserById(1L).isPresent()) {
            User user;
            user = userDbStorage.getUserById(1L).get();
            user.setName("userXXX");
            userDbStorage.updateUser(user);
            assertThat(userDbStorage.getUserById(1L)).get().hasFieldOrPropertyWithValue("name", "userXXX");
        }
    }

    @Test
    public void getAllUsers() {
        List<User> users = userDbStorage.getAllUsers();
        assertThat(users).size().isEqualTo(5);
    }

    @Test
    public void addFriendTest() {

        assertThat(friendService.getFriends(1).size()).isEqualTo(1);
        friendService.addFriend(1L, 2L);
        friendService.addFriend(2L, 1L);

        assertThat(friendService.getFriends(1).size()).isEqualTo(1);
    }

    @Test
    public void deleteFriendTest() {
        assertThat(friendService.getFriends(1).size()).isEqualTo(1);
        friendService.removeFriend(1L, 2L);
        friendService.removeFriend(2L, 1L);

        assertThat(friendService.getFriends(1).size()).isEqualTo(0);
    }

    @Test
    public void getFriends() {
        assertThat(friendService.getFriends(1).size()).isEqualTo(1);
    }
}