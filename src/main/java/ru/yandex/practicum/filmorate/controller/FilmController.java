package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.trace("Обращение на endpoint GET /films");
        log.trace("Фильм в запросе: {}", film);
        log.debug("Текущий список фильмов: {}", films);


        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} записан", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.trace("Обращение на endpoint PUT /films");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Нет такого фильма");
        }
        validateFilm(film);
        log.trace("Валидация PUT /films прошла");
        log.debug("Cписок фильмов до обновления фильма : {}", films);
        films.put(film.getId(), film);
        log.trace("Фильм {} записан", film);
        log.debug("Cписок фильмов после обновления фильма : {}", films);
        log.info("Фильм {} обновлён", film.getId());
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
        int DESCRIPTION_SIZE = 200;
        log.trace("Начало процесса валидации фильма");
        if (film.getName() == null || film.getName().isEmpty()) {
            log.trace("Валидация названия не пройдена, название пусто name={}", film.getName());
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription().length() > DESCRIPTION_SIZE) {
            log.trace("Валидация описания не пройдена, слишком длинное описание, длина {}, description={}",
                    film.getDescription().length(), film.getDescription());
            throw new ValidationException("Описание больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)) {
            log.trace("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28");
        }
        if (film.getDuration() < 0) {
            log.trace("Валидация продолжительности не пройдена, значение отрицательное duration={}",
                    film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
