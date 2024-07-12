package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class User {
    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
}