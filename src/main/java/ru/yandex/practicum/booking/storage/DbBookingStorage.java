package ru.yandex.practicum.booking.storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.*;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.item.ItemRepository;
import ru.yandex.practicum.user.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DbBookingStorage implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingsMapper bookingsMapper;

    @Override
    public Booking requestBooking(int requesterId, RequestBooking requestBooking) {
        Booking booking = bookingsMapper.requestBookingToBooking(requestBooking);
        booking.setBooker(userRepository.findById(requesterId).get());
        validateRequestBooking(requesterId, booking);
        Item item = booking.getItem();
        item.getBookingList().add(booking);
        bookingRepository.save(booking);
        itemRepository.save(item);
        return booking;
    }

    private void validateRequestBooking(int requesterId, Booking booking) {
        Timestamp end = booking.getEnd();
        Timestamp start = booking.getStart();
        if (end.before(Timestamp.from(Instant.now()))) {
            throw new BookingException("Дата окончания бронирования в прошлом");
        }
        if (end.before(start)) {
            throw new BookingException("Дата окончания не может быть раньше даты начала");
        }
        if (start.before(Timestamp.from(Instant.now()))) {
            throw new BookingException("Дата начала бронирования в прошлом");
        }
        if (requesterId == booking.getItem().getOwner().getId()) {
            throw new ObjectNotFoundException("Пользователь не может бронировать свою вещь");
        }
        if (!booking.getItem().getAvailable()) {
            throw new BookingException("Вещь не доступна для бронирования");
        }
    }

    @Override
    public Booking responseBooking(int userId, int bookingId, boolean isApproved) {
        Booking booking = null;
        if (bookingRepository.findById(bookingId).isPresent()) {
            booking = bookingRepository.findById(bookingId).get();
        } else {
            throw new ObjectNotFoundException("Указанного запроса не существует");
        }
        validateResponse(userId, booking, isApproved);
        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    private void validateResponse(int userId, Booking booking, boolean isApproved) {
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ObjectNotFoundException("Вы не можете менять вещь так как не являетесь владельцем");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new BookingException("Вещь уже изменила статус");
        }
    }

    @Override
    public Optional<ResponseBooking> getBookingById(int bookingId, int requesterId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        validateGetBooking(requesterId, optionalBooking);
        ResponseBooking responseBooking = bookingsMapper.bookingToResponseBooking(optionalBooking.get());
        return Optional.of(responseBooking);
    }

    private void validateGetBooking(int requesterId, Optional<Booking> optionalBooking) {
        if (optionalBooking.isEmpty()) {
            throw new ObjectNotFoundException("Указанного запроса не существует");
        }
        if (requesterId != optionalBooking.get().getItem().getOwner().getId() &&
                optionalBooking.get().getBooker().getId() != requesterId) {
            throw new ObjectNotFoundException("Вы не являетесь пользователем или владельцем этой вещи");
        }
    }

    private StateBooking validateStateAndBookerForGet(int bookerId, String state) {
        if (state == null) state = "ALL";
        if (!EnumUtils.isValidEnum(StateBooking.class, state)) {
            throw new BookingException("Unknown state: " + state.toUpperCase());
        }
        checkExistenceForUser(bookerId);
        return StateBooking.valueOf(state);
    }

    @Override
    public List<ResponseBooking> getListBookingForUser(int bookerId, String state) {
        StateBooking stateBooking = validateStateAndBookerForGet(bookerId, state);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 10100000);
        List<ResponseBooking> bookingList = null;
        switch (stateBooking) {
            case ALL:
                bookingList = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId).stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookingList = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartAsc(bookerId,
                                new Timestamp(System.currentTimeMillis() + 10800000),
                                new Timestamp(System.currentTimeMillis() + 10800000))
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingList = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartAsc(bookerId,
                                new Timestamp(System.currentTimeMillis() + 10800000))
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingList = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(bookerId, timestamp)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookingList = bookingRepository.findByBooker_IdAndStatusOrderByStartAsc(bookerId, BookingStatus.WAITING)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingList = bookingRepository.findByBooker_IdAndStatusOrderByStartAsc(bookerId, BookingStatus.REJECTED)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
        }
        return bookingList;
    }

    private void checkExistenceForUser(int bookerId) {
        if (userRepository.findById(bookerId).isEmpty()) {
            throw new ObjectNotFoundException("Вы не являетесь пользователем или владельцем этой вещи");
        }
    }

    public List<ResponseBooking> getListBookingForOwner(int bookerId, String state) {
        StateBooking stateBooking = validateStateAndBookerForGet(bookerId, state);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 10100000);
        List<ResponseBooking> bookingList = null;
        switch (stateBooking) {
            case ALL:
                bookingList = bookingRepository.getAllBookingsForOwner(bookerId).stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookingList = bookingRepository.getCurrentBookingsForOwner(bookerId,
                                new Timestamp(System.currentTimeMillis() + 10800000),
                                new Timestamp(System.currentTimeMillis() + 10800000))
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingList = bookingRepository.getPastBookingsForOwner(bookerId,
                                new Timestamp(System.currentTimeMillis() + 10800000))
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingList = bookingRepository.getFutureBookingsForOwner(bookerId, timestamp)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case WAITING:
                bookingList = bookingRepository.getBookingsForOwnerWithStatus(bookerId, BookingStatus.WAITING)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingList = bookingRepository.getBookingsForOwnerWithStatus(bookerId, BookingStatus.REJECTED)
                        .stream()
                        .map(bookingsMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
        }
        return bookingList;
    }


    @Override
    public List<Booking> getListBookingForAllItemsUser(int userId, StateBooking stateBooking) {
        return null;
    }
}
