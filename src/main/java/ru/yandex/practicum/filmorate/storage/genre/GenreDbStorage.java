package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class GenreDbStorage implements GenreStorage {

    GenreRowMapper genreRowMapper;
    JdbcTemplate jdbcTemplate;
    private static final String FIND_ALL_GENRES = "SELECT * FROM genre ORDER BY genre_id ASC";
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genre WHERE genre_id =?";

    @Override
    public Optional<Genre> getGenreById(int id) {

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_GENRE_BY_ID, genreRowMapper, id));
        } catch (
                EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenresNames() {
        return jdbcTemplate.query(FIND_ALL_GENRES, genreRowMapper);
    }
}
