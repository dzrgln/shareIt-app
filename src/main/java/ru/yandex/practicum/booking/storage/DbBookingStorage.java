package ru.yandex.practicum.booking.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.booking.*;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.exceptions.BookingException;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.user.UserRepository;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
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
    private final BookingMapper bookingMapper;

    @Override
    public Booking requestBooking(int requesterId, RequestBooking requestBooking) {
        Booking booking = bookingMapper.requestBookingToBooking(requestBooking);
        booking.setBooker(userRepository.findById(requesterId).get());
        validateRequestBooking(requesterId, booking);
        return bookingRepository.save(booking);
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

    private void validateResponse(int userId, Booking booking, boolean isApproved){
        if(booking.getItem().getOwner().getId() != userId){
            throw new ObjectNotFoundException("Вы не можете менять вещь так как не являетесь владельцем");
        }
        if(!booking.getStatus().equals(BookingStatus.WAITING) ){
            throw new BookingException("Вещь уже изменила статус");
        }
    }

    @Override
    public Optional<ResponseBooking> getBookingById(int bookingId, int requesterId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        validateGetBooking(requesterId, optionalBooking);
        ResponseBooking responseBooking = bookingMapper.bookingToResponseBooking(optionalBooking.get());
        return Optional.of(responseBooking);
    }

    private void validateGetBooking(int requesterId, Optional<Booking> optionalBooking) {
        if (optionalBooking.isEmpty()) {
            throw new ObjectNotFoundException("Указанного запроса не существует");
        }
        if (requesterId != optionalBooking.get().getItem().getOwner().getId() &&
                optionalBooking.get().getBooker().getId() != requesterId) {
            System.out.println("itemId " + optionalBooking.get().getItem().getId());
            System.out.println("bookerId " + optionalBooking.get().getBooker().getId());
            System.out.println("ownerId " + optionalBooking.get().getItem().getOwner().getId());
            System.out.println("requesterId " + requesterId);
            throw new ObjectNotFoundException("Вы не являетесь пользователем или владельцем этой вещи");
        }
    }

    @Override
    public List<ResponseBooking> getListBookingForUser(int bookerId, String state) {
        if (state == null) state = "ALL";
        StateBooking stateBooking = StateBooking.valueOf(state);
        System.out.println("in the mothod");
        List<ResponseBooking> bookingList = null;
        switch (stateBooking) {
            case ALL:
                bookingList = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId).stream()
                        .map(bookingMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
//            case CURRENT:
//                bookingList = bookingRepository.findByBooker_IdAndEndIsBeforeAndStartIsAfterOrderByStartDesc()
            default:
                bookingList = bookingRepository.findByBooker_IdOrderByStartDesc(bookerId).stream()
                        .map(bookingMapper::bookingToResponseBooking)
                        .collect(Collectors.toList());
                break;
        }
        return bookingList;
    }

    public List<ResponseBooking> getListBookingForOwner(int bookerId) {
        return bookingRepository.findByOwner(bookerId).stream()
                .map(bookingMapper::bookingToResponseBooking)
                .collect(Collectors.toList());
    }


    @Override
    public List<Booking> getListBookingForAllItemsUser(int userId, StateBooking stateBooking) {
        return null;
    }
}
