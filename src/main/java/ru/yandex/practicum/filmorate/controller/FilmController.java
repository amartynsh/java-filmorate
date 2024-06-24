package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Обращение на endpoint GET /films");
        log.info("Фильм в запросе: {}", film);
        log.info("Текущий список фильмов: {}", films);


        validateFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} записан", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обращение на endpoint PUT /films");
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Нет такого фильма");
        }
        validateFilm(film);
        log.info("Валидация PUT /films прошла");
        log.info("Cписок фильмов до обновления фильма : {}", films);
        films.put(film.getId(), film);
        log.info("Фильм {} обновлён", film);
        log.info("Cписок фильмов после обновления фильма : {}", films);
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
        LocalDate firstFilmReleaseDate = LocalDate.of(1895, 12, 28);
        int descriptionSize = 200;
        log.info("Начало процесса валидации фильма");
        if (film.getName() == null || film.getName().isEmpty()) {
            log.trace("Валидация названия не пройдена, название пусто name={}", film.getName());
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription().length() > descriptionSize) {
            log.info("Валидация описания не пройдена, слишком длинное описание, длина {}, description={}",
                    film.getDescription().length(), film.getDescription());
            throw new ValidationException("Описание больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(firstFilmReleaseDate)) {
            log.info("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28");
        }
        if (film.getDuration() < 0) {
            log.info("Валидация продолжительности не пройдена, значение отрицательное duration={}",
                    film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }
}
