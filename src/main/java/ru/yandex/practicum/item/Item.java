package ru.yandex.practicum.item;

import lombok.*;
import ru.yandex.practicum.exceptions.ErrorHandler;
import ru.yandex.practicum.request.ItemRequest;
import ru.yandex.practicum.user.User;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
@RequiredArgsConstructor

public class Item {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull()
    private String description;
    private User owner;
    @NotNull
    @AssertTrue
    private boolean available;
    private ItemRequest request;
}
