package ru.yandex.practicum.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
