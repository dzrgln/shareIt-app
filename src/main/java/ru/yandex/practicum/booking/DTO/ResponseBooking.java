package ru.yandex.practicum.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBooking {
    private int id;
    private String start;
    private String end;
    private BookingStatus status;
    private User booker;
    private Item item;
}
