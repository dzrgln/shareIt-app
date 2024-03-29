package ru.yandex.practicum.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.booking.DTO.ResponseBookingForItem;
import ru.yandex.practicum.item.storage.DbItemService;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

@Mapper(componentModel = "spring",
        uses = {DbItemService.class, User.class},
        imports = {LocalDateTime.class, TemporalAccessor.class})

public interface BookingsMapper {
    @Mapping(target = "item", source = "itemId")
    Booking requestBookingToBooking(RequestBooking requestBooking);

    @Mapping(target = "start", expression = "java(booking.getStart().toString())")
    @Mapping(target = "end", expression = "java(booking.getEnd().toString())")
    ResponseBooking bookingToResponseBooking(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    ResponseBookingForItem bookingToResponseBookingForItem(Booking booking);

    @Mapping(target = "bookerId", source = "booker.id")
    ResponseBookingForItem responseBookingToResponseBookingForItem(ResponseBooking responseBooking);


}

