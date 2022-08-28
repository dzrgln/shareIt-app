package ru.yandex.practicum.booking.DTO;

import lombok.*;
import ru.yandex.practicum.booking.BookingStatus;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class RequestBooking {
    private int itemId;
    private Timestamp start;
    private Timestamp end;
    private BookingStatus status = BookingStatus.WAITING;

  //  LocalDateTime time = LocalDateTime.from(start);
}
