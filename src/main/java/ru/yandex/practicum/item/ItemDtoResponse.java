package ru.yandex.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.DTO.ResponseBookingForItem;
import ru.yandex.practicum.comment.ResponseDtoComment;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemDtoResponse {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private ResponseBookingForItem lastBooking;
    private ResponseBookingForItem nextBooking;
    private List<ResponseDtoComment> comments;
}
