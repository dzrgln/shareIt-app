package ru.yandex.practicum.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBookingForItem {
    private int id;
    private int bookerId;
}
