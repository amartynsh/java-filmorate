package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
//Без сортировки не проходит тесты Postman
public class Genre implements Comparable<Genre> {
    private int id;
    private String name;

    public Genre() {
    }

    @Override
    public int compareTo(Genre other) {
        return Integer.compare(this.id, other.id);
    }
}