package ru.yandex.practicum.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.BookingStatus;

import java.sql.Timestamp;

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
