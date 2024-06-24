package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class User {
    private int id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String login;
    private String name;
    private LocalDate birthday;
}
