package ru.yandex.practicum.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.BookingStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBooking {
    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status = BookingStatus.WAITING;
}
