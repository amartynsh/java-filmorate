package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long userId = 1;

    @Override
    public Optional<Film> addFilm(Film film) {
        log.trace("Текущий список фильмов: {}", films);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} записан", film.getId());
        return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Обновляемый фильм не найден");
        }
        log.trace("Cписок фильмов до обновления фильма : {}", films);
        films.put(film.getId(), film);
        log.trace("Cписок фильмов после обновления фильма : {}", films);
        log.info("Фильм {} обновлён", film.getId());
        return Optional.of(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private long getNextId() {
        return userId++;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return films.values().stream().filter(f -> f.getId() == id).findFirst();
    }

    @Override
    public void validateFilmSql(Film film) {

    }

    @Override
    public void addLikeToFilm(long filmId, long userId) {

    }

    @Override
    public void deleteLikeFromFilm(long filmId, long userId) {

    }
}