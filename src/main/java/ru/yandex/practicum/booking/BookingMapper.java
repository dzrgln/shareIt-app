package ru.yandex.practicum.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.item.storage.DbItemStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Mapper(componentModel = "spring",
        uses = {DbItemStorage.class},
        imports = {LocalDateTime.class, TemporalAccessor.class})

public interface BookingMapper {

    @Mapping(target = "item", source = "itemId")
    Booking requestBookingToBooking(RequestBooking requestBooking);

    @Mapping(target = "start", expression = "java(booking.getStart().toLocalDateTime().minusHours(3).toString())")
    @Mapping(target = "end", expression = "java(booking.getEnd().toLocalDateTime().minusHours(3).toString())")
    ResponseBooking bookingToResponseBooking(Booking booking);

}

