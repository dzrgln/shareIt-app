package ru.yandex.practicum.booking.storage;

import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.booking.StateBooking;

import java.util.List;
import java.util.Optional;

public interface BookingStorage {
    Booking requestBooking(int requesterId, RequestBooking requestBooking);

    Booking responseBooking(int userId, int bookingId, boolean isApproved);

    Optional<ResponseBooking> getBookingById(int bookingId, int requesterId);

    List<ResponseBooking> getListBookingForUser(int bookerId, String state);
    List<ResponseBooking> getListBookingForOwner(int bookerId, String state);



    List<Booking> getListBookingForAllItemsUser(int userId, StateBooking stateBooking);
}
