package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;


import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;
    UserRowMapper userRowMapper;

    private static final String ADD_USER_SQL = "INSERT INTO users (login, name, email,  birthday)" +
            "    VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER_SQL = "UPDATE users SET login =?, name =?, email =?,  birthday =? " +
            "WHERE user_id =?";
    private static final String GET_ALL_USERS = "SELECT * FROM public.users";
    private static final String GET_USER_BY_ID = "SELECT * FROM public.users WHERE user_id = ?";
    private static final String DELETE_USER_FRIEND = "DELETE FROM friends WHERE user_id =? AND friend_id =?";
    private static final String ADD_USER_FRIEND = "INSERT INTO friends (user_id, friend_id) VALUES (?,?)";
    private static final String GET_FRIENDS_SQL = "SELECT * FROM public.users WHERE user_id IN " +
            "(SELECT friend_id FROM friends WHERE user_id = ?)";
    private static final String DELETE_USER_SQL = "DELETE FROM public.users WHERE user_id = ?";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public User addUser(User user) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        long key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        log.info("Ключ{}", key);
        return getUserById(key).get();
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(UPDATE_USER_SQL);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            statement.setLong(5, user.getId());
            return statement;
        });
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(GET_ALL_USERS, userRowMapper);
    }

    @Override
    public Optional<User> getUserById(long id) {
        try {
            Optional<User> user = Optional.ofNullable(jdbcTemplate.queryForObject(GET_USER_BY_ID, userRowMapper, id));
            log.info("Сделали запрос sql:  {}, с ID {} ", GET_USER_BY_ID, id);
            return user;
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUserById(long id) {
        jdbcTemplate.update(DELETE_USER_SQL, id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(ADD_USER_FRIEND, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_USER_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        return jdbcTemplate.query(GET_FRIENDS_SQL, userRowMapper, userId);
    }


}