package ru.yandex.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.request.Request;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemDtoResponse {
    private int id;

    private String name;

    private String description;

    private boolean available;
 //   private Request request;
    private List<Booking> bookingList;

}
