package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    FilmStorage filmStorage = new InMemoryFilmStorage();
    UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        filmStorage.updateFilm(film);
        return film;
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

}
