package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(long id);

    void addLikeToFilm(long filmId, long userId);

    void deleteLikeFromFilm(long filmId, long userId);
}
