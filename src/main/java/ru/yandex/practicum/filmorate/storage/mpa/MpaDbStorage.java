
package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {
    MpaRowMapper mpaRowMapper;
    JdbcTemplate jdbcTemplate;
    private static final String GET_MPA_BY_ID = "SELECT * FROM mpa WHERE mpa_id =?";
    private static final String GET_ALL_MPA = "SELECT * FROM mpa ORDER BY mpa_id ASC";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper mpaRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRowMapper = mpaRowMapper;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_MPA_BY_ID, mpaRowMapper, id));
        } catch (
                EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpaNames() {
        return jdbcTemplate.query(GET_ALL_MPA, mpaRowMapper);
    }


}
