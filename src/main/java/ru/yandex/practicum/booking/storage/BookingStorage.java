package ru.yandex.practicum.booking.storage;

import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.StateBooking;

import java.util.List;
import java.util.Optional;

public interface BookingStorage {
    Booking requestBooking(int requesterId, Booking booking);

    Booking responseBooking(int bookingId, boolean isApproved);

    Optional<Booking> getBookingById(int bookingId);

    List<Booking> getListBookingForUser(int userId, StateBooking stateBooking);

    List<Booking> getListBookingForAllItemsUser(int userId, StateBooking stateBooking);
}
