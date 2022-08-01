package ru.yandex.practicum.user;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;

@Data
public class User {
    private int id;
    private String name;
    @NonNull
    @Email
    private String email;

    public User(int id, String name, @NonNull String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
