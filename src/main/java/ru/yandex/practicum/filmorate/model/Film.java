package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    private int id;
    @NotEmpty
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
