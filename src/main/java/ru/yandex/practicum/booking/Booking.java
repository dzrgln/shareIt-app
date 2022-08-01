package ru.yandex.practicum.booking;

import lombok.Data;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.time.LocalDate;

@Data
public class Booking {
    private int id;
    private Item item;
    private User booker;
    private LocalDate start;
    private LocalDate end;
    private BookingStatus status;
}
