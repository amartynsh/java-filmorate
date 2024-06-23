package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void addFilmShouldBeSuccsess() {
        Film film = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();
        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void addFilmShouldThrowValidateException() {
        //Недопустимая дата < 1895г
        Film filmWrongYear = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(88)
                .build();

        //Проверяем отрицательную продолжительность фильма
        Film filmNegativeDuration = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(-1)
                .build();

        assertThrows(ValidationException.class, () -> filmController.addFilm(filmWrongYear));
        assertThrows(ValidationException.class, () -> filmController.addFilm(filmNegativeDuration));

        //Фильм с описанием длинее 200 символов (201)
        Film filmTooLongDescription = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия, Советская фантастическая комедия, Советская фантастическая комедия, Советская фантастическая комедия, \" +\n" +
                        "Советская фантастическая комедия, Советская фантастическая ко")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();

        assertThrows(ValidationException.class, () -> filmController.addFilm(filmTooLongDescription));
    }

    @Test
    void updateFilmShouldBeSuccess() {
        //Сначала добавляем фильм
        Film film = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();

        assertDoesNotThrow(() -> filmController.addFilm(film));

        Film updatedFilm = Film.builder()
                .id(film.getId())
                .name("Иван Васильевич меняет профессию")
                .description("Советская научно-фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();

        assertDoesNotThrow(() -> filmController.addFilm(updatedFilm));
    }

    @Test
    void updateFilmShouldThrowValidateException() {
        Film film = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();
        assertDoesNotThrow(() -> filmController.addFilm(film));

        //Неуказан ID обновляемого фильма
        Film updatedFilm = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская научно-фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();

        assertThrows(ValidationException.class, () -> filmController.updateFilm(updatedFilm));
    }

    @Test
    void getAllFilms() {
        Film film = Film.builder()
                .name("Иван Васильевич меняет профессию")
                .description("Советская фантастическая комедия")
                .releaseDate(LocalDate.of(1973, 9, 27))
                .duration(88)
                .build();
        assertDoesNotThrow(() -> filmController.addFilm(film));

        Film film1 = Film.builder()
                .name("Полосатый рейс")
                .description("Советское судно везет ценный груз — хищников для зоопарка.")
                .releaseDate(LocalDate.of(1961, 6, 27))
                .duration(87)
                .build();
        assertDoesNotThrow(() -> filmController.addFilm(film1));
        List<Film> films = filmController.getAllFilms();
        assertTrue(films.contains(film) && films.contains(film1));
    }
}