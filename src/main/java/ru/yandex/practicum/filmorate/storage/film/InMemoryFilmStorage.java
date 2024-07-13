package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
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
    public Film addFilm(Film film) {
        log.info("Текущий список фильмов: {}", films);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} записан", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(@Valid Film film) {

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Обновляемый фильм не найден");
        }
        log.info("Валидация PUT /films прошла");
        log.info("Cписок фильмов до обновления фильма : {}", films);
        films.put(film.getId(), film);
        log.info("Фильм {} обновлён", film);
        log.info("Cписок фильмов после обновления фильма : {}", films);
        log.info("Фильм {} обновлён", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private long getNextId() {
        return userId++;
    }

    @Override
    public Film getFilmById(long id) {
        return Optional.ofNullable(films.get(id)).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }
}