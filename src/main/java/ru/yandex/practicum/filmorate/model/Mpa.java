package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Mpa {
    private int id;
    private String name;

    public Mpa() {

    }

    public Mpa(int mpaId) {
    }
}
