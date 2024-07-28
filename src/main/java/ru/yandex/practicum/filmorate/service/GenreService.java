package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class GenreService {
    GenreStorage genreStorage;

    public Genre getGenreById(int id) {
        if (genreStorage.getGenreById(id).isEmpty()) {
            throw new NotFoundException("Genre " + id + " not found");
        }
        return genreStorage.getGenreById(id).get();
    }

    public List<Genre> getAllGenres() {
        log.info("Getting all genres");
        return genreStorage.getAllGenresNames();
    }

}
