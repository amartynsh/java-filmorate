
package ru.yandex.practicum.filmorate.mapper;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
@Component
public class FilmRowMapper implements RowMapper<Film> {
    private static final String GENRE_REQUEST = """
            SELECT * FROM public.films_genre_mapping AS fgm
            JOIN GENRE AS g ON FGM.GENRE_ID = g.genre_id
            WHERE fgm.film_id = ?""";
    private static final String GET_MPA = "SELECT * FROM mpa WHERE mpa.mpa_id = ?";
    private static final String GET_LIKES = "SELECT * FROM likes WHERE film_id= ?";

    JdbcTemplate jdbcTemplate;
    GenreRowMapper genreRowMapper;
    MpaRowMapper mpaRowMapper;
    LikesRowMapper likesRowMapper;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Integer mpaId = resultSet.getInt("mpa_id");
        Optional<Mpa> mpa = Optional.ofNullable(jdbcTemplate.queryForObject(GET_MPA, mpaRowMapper, mpaId));

        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(GENRE_REQUEST, genreRowMapper,
                resultSet.getLong("film_id")));

        List<Long> likes = jdbcTemplate.query(GET_LIKES, likesRowMapper, resultSet.getLong("film_id"));

        Film film = Film.builder()
                .id((resultSet.getLong("film_id")))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpa.orElse(null))
                .build();

        film.getGenres().addAll(genres);
        film.getLikes().addAll(likes);

        return film;
    }
}
