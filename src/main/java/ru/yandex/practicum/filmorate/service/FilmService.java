package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    LocalDate firstFilmReleaseDate = LocalDate.of(1895, 12, 28);

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film).get();
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        getFilmById(film.getId());
        filmStorage.updateFilm(film);
        return getFilmById(film.getId());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getMostLikedFilms(int count) {
        log.info("Вызвали метод подсчета популярных фильмов, надо отдать= {}", count);
        List<Film> sortedFilm = new ArrayList<>(filmStorage.getAllFilms());
        sortedFilm = sortedFilm.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

        log.info("Получился список {}", sortedFilm);

        return sortedFilm;
    }

    public void addLikeToFilm(long filmId, long userId) {
        log.info("Для фильма {} добавляем лайк от пользователя {}", filmId, userId);
        //Проверяем наличие пользователя
        userService.getUserById(userId);
        log.info("Состояние пользователя: {}", userService.getUserById(userId));
        log.info("Лайки фильма ДО обновления {}", filmStorage.getFilmById(filmId).get().getLikes());
        filmStorage.addLikeToFilm(filmId, userId);
        log.info("Лайки фильма после обновления {}", filmStorage.getFilmById(filmId).get().getLikes());
    }

    public void deleteLikeFromFilm(long filmId, long userId) {
        //Проверяем наличие пользователя, чей лайк удаляем
        userService.getUserById(userId);
        filmStorage.deleteLikeFromFilm(filmId, userId);
    }

    private void validateFilm(Film film) {
        log.info("Начало процесса валидации фильма");
        if (film.getReleaseDate().isBefore(firstFilmReleaseDate)) {
            log.info("Валидация даты не пройдена, значение releaseDate={}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше чем " + firstFilmReleaseDate);
        }
    }

    public Film getFilmById(long filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм " + filmId + " не найден");
        }
        return filmStorage.getFilmById(filmId).get();
    }
}