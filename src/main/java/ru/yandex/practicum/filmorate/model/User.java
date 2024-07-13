package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
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

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Long> friends = new HashSet<>();
}