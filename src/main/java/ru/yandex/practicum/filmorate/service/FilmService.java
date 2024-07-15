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
    LocalDate firstFilmReleaseDate = LocalDate.of(1895, 12, 28);

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
        log.trace("Проверяем есть ли сервисы ? {} {}", filmStorage, userService);
        log.info("Для фильма {} добавляем лайк от пользователя {}", filmId, userId);

        //Проверяем наличие пользователя
        userService.getUserById(userId);
        getFilmById(filmId).getLikes().add(userId);
    }

    public void deleteLikeFromFilm(long filmId, long userId) {

        //Проверяем наличие пользователя, чей лайк удаляем
        userService.getUserById(userId);
        getFilmById(filmId).getLikes().remove(userId);

    }

    private void validateFilm(Film film) {
        log.info("Начало процесса валидации фильма");
        if (film.getReleaseDate().isBefore(firstFilmReleaseDate)) {
            log.info("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28");
        }
    }

    public Film getFilmById(long filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmStorage.getFilmById(filmId).get();
    }
}