package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        log.info("Текущий список фильмов: {}", films);
        validateFilm(film);
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Фильм {} записан", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(@Valid Film film) {

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Обновляемый фильм не найден");
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

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        LocalDate firstFilmReleaseDate = LocalDate.of(1895, 12, 28);
        int descriptionSize = 200;
        InMemoryFilmStorage.log.info("Начало процесса валидации фильма");
        if (film.getName() == null || film.getName().isEmpty()) {
            InMemoryFilmStorage.log.trace("Валидация названия не пройдена, название пусто name={}", film.getName());
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDescription().length() > descriptionSize) {
            InMemoryFilmStorage.log.info("Валидация описания не пройдена, слишком длинное описание, длина {}, description={}",
                    film.getDescription().length(), film.getDescription());
            throw new ValidationException("Описание больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(firstFilmReleaseDate)) {
            InMemoryFilmStorage.log.info("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28");
        }
        if (film.getDuration() < 0) {
            InMemoryFilmStorage.log.info("Валидация продолжительности не пройдена, значение отрицательное duration={}",
                    film.getDuration());
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film getFilmById(long id) {
        if (films.get(id) == null) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }
}