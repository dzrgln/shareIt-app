package ru.yandex.practicum.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.item.ItemDtoResponse;
import ru.yandex.practicum.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBooking {
    private int id;
    private String start;
    private String end;
    private BookingStatus status;
    private User booker;
    private ItemDtoResponse item;
}
