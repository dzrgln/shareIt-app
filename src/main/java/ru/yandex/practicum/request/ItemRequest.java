package ru.yandex.practicum.request;

import lombok.Data;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private int id;
    private String description;
    private User requester;
    private LocalDateTime created;
}
