package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(@Valid Film film);

    List<Film> getAllFilms();

    Film getFilmById(long id);

}
