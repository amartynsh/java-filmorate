package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public void addFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) {
        validateFilm(film);
        filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getMostLikedFilms(int count) {
        List<Film> sortedFilm = new ArrayList<>(filmStorage.getAllFilms());
        return sortedFilm.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .toList();
    }

    public void addLikeToFilm(long filmId, long userId) {
        log.info("Проверяем есть ли сервисы ? {} {}", filmStorage, userService);
        log.info("Для фильма {} добавляем лайк от пользователя {}", filmId, userId);
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("При добавлении лайка фильм не найден");
        }
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("При добавлении лайка пользователь не найден");
        }

        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);

        log.info("Всего лайков у фильма{} ", film.getLikes().size());
        log.info("Обновленный список лайков {} ", film.getLikes());
    }

    public void deleteLikeFromFilm(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("При добавлении лайка фильм не найден");

        }
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("При добавлении лайка пользователь не найден");
        }
        Film film = filmStorage.getFilmById(filmId);
        log.info("Для фильма {} удалям лайк от пользователя {}", film.getId(), userId);
        film.getLikes().remove(userId);
        log.info("Новый список лайков {} ", film.getLikes());
    }

    private void validateFilm(Film film) {
        LocalDate firstFilmReleaseDate = LocalDate.of(1895, 12, 28);
        log.info("Начало процесса валидации фильма");
        if (film.getReleaseDate().isBefore(firstFilmReleaseDate)) {
            log.info("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28");
        }
    }
}