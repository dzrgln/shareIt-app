package ru.yandex.practicum.booking.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.booking.BookingRepository;
import ru.yandex.practicum.booking.BookingStatus;
import ru.yandex.practicum.booking.StateBooking;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public Booking requestBooking(int requesterId, Booking booking) {
        booking.setBooker(userRepository.findById(requesterId).get());
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking responseBooking(int bookingId, boolean isApproved) {
        Booking booking = null;
        if (bookingRepository.findById(bookingId).isPresent()) {
            booking = bookingRepository.findById(bookingId).get();
        } else {
            throw new ObjectNotFoundException("Указанного запроса не существует");
        }
        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Override
    public List<Booking> getListBookingForUser(int userId, StateBooking stateBooking) {
        return null;
    }

    @Override
    public List<Booking> getListBookingForAllItemsUser(int userId, StateBooking stateBooking) {
        return null;
    }
}
