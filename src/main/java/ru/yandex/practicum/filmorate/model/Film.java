package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Film {
    protected int id;
    protected String name;
    protected String description;
    private LocalDate releaseDate;
    protected int duration;
}
