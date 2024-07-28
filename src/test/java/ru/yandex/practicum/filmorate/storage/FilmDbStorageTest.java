package ru.yandex.practicum.filmorate.storage;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;


@AutoConfigureTestDatabase
@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = {"/testconf/schema.sql", "/testconf/fortest1.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/testconf/drop.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)


public class FilmDbStorageTest {

    @Autowired
    public FilmDbStorage filmDbStorage;

    @Test
    public void findFilmLikesTest() {
        Film film = filmDbStorage.getFilmById(1).get();
        assertThat(film.getLikes()).size().isEqualTo(3);
    }

    @Test
    public void deleteLikeTest() {
        filmDbStorage.getFilmById(1L);
        filmDbStorage.addLikeToFilm(1L, 1);
        filmDbStorage.deleteLikeFromFilm(1L, 1L);
        List<Long> likes = filmDbStorage.getFilmById(1).get().getLikes().stream().toList();
        assertThat(likes).size().isEqualTo(2);
        assertThat(likes).doesNotContain(1L);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmDbStorage.getAllFilms();
        assertThat(films).size().isEqualTo(3);
    }

    @Test
    public void getFilmByIdTest() {
        Film film = filmDbStorage.getFilmById(1).get();
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void addFilmTest() {
        Film film = new Film(0, "test", "test", LocalDate.now()
                , 60, new Mpa(5, "1"));
        filmDbStorage.addFilm(film);
        assertThat(filmDbStorage.getFilmById(4).get()).hasFieldOrPropertyWithValue("name", "test");
    }
}




