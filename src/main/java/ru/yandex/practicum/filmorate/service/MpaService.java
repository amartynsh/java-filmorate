package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;


@Service

public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> findAll() {
        return mpaStorage.getAllMpaNames();
    }

    public Mpa findMpaById(int id) {
        if (mpaStorage.getMpaById(id).isEmpty()) {
            throw new NotFoundException("Mpa not found");
        }
        return mpaStorage.getMpaById(id).get();
    }
}

