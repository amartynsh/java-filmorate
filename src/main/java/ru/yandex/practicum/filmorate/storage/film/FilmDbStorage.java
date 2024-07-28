package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.mapper.LikesRowMapper;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@AllArgsConstructor
@Component

public class FilmDbStorage implements FilmStorage {

    private static final String FILM_ADD = "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
            "    VALUES (?, ?, ?, ?, ?)";
    private static final   String SQL_FILM_UPDATE = "UPDATE films SET description =?, name =?, release_date =?, " +
            "duration =?, mpa_id =? WHERE film_id =?";
    private static final   String GET_ALL_FILMS = "SELECT * FROM public.films";
    private static final String GET_FILM_BY_ID = "SELECT * FROM public.films AS f WHERE f.film_id = ?";
    private static final   String MPA_BY_ID = "select COUNT(mpa.mpa_id) FROM public.mpa AS mpa WHERE mpa.mpa_id = ? ";
    private static final   String FIND_GENRE_BY_ID = "select COUNT (*) FROM GENRE AS g WHERE genre_id = ?  ";
    private static final   String ADD_GENRE_TO_FILM = "INSERT INTO public.films_genre_mapping(genre_id, " +
            "film_id) VALUES (?,?)";
    private static final   String ADD_LIKES_TO_FILM = "INSERT INTO likes (film_id, user_id) VALUES (?,?)";
    private static final   String DELETE_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    JdbcTemplate jdbcTemplate;
    MpaRowMapper mpaRowMapper;
    GenreRowMapper genreRowMapper;
    LikesRowMapper likesRowMapper;
    FilmRowMapper filmRowMapper;


    @Override
    public Optional<Film> addFilm(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(FILM_ADD,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        for (Genre genre : film.getGenres()) {
            addGenreToFilm(genre.getId(), key);
        }
        return getFilmById(key);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        log.info("Начали обновлять фильм");
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(SQL_FILM_UPDATE);
            statement.setString(1, film.getDescription());
            statement.setString(2, film.getName());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setLong(5, film.getMpa().getId());
            statement.setLong(6, film.getId());
            return statement;
        });
        log.info("Обновили фильм  - обновление жанров");
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                addGenreToFilm(genre.getId(), film.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(GET_ALL_FILMS, filmRowMapper);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_FILM_BY_ID, filmRowMapper, id));
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void validateFilmSql(Film film) {
        log.info("Начали проверять MPA SQL");
        Integer countMpa = jdbcTemplate.queryForObject(MPA_BY_ID, Integer.class, film.getMpa().getId());
        if (countMpa == null || countMpa == 0) {
            throw new ValidationException("MPA: " + film.getMpa().getId() + " не найдено");
        }

        for (Genre genre : film.getGenres()) {
            Integer countGenre = jdbcTemplate.queryForObject(FIND_GENRE_BY_ID, Integer.class, genre.getId());
            if (countGenre == null || countGenre == 0) {
                throw new ValidationException("Жанр: " + genre.getId() + " не найден");
            }
        }
    }

    private void addGenreToFilm(int genreId, long filmId) {
        log.info("обновляем жанры");
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_GENRE_TO_FILM);
            statement.setInt(1, genreId);
            statement.setLong(2, filmId);
            return statement;
        });
    }

    @Override
    public void addLikeToFilm(long filmId, long userId) {

        log.info("добавляем лайки");
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(ADD_LIKES_TO_FILM);
            statement.setLong(1, filmId);
            statement.setLong(2, userId);
            return statement;
        });
    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) {
        log.info("удаляем лайки");
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETE_LIKE);
            statement.setLong(1, filmId);
            statement.setLong(2, userId);
            return statement;
        });
    }
}