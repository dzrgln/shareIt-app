package ru.yandex.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDtoComment {
    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
