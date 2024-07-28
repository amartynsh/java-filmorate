package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FriendsDbStorage {
    private static final String ADD_FRIEND = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
    JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(int userId, int friendId) {
        jdbcTemplate.update(ADD_FRIEND, userId, friendId);
    }


}
