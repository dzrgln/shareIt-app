package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.booking.storage.BookingStorage;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingStorage bookingStorage;

    @PostMapping
    public Booking requestBooking(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                  @RequestBody RequestBooking requestBooking) {
        Booking newBooking = bookingStorage.requestBooking(requesterId, requestBooking);
        log.info("Создано бронирование '{}'", newBooking);
        return newBooking;
    }


    @PatchMapping("/{itemId}")
    public Booking responseBooking(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                   @PathVariable("itemId") int itemId,
                                   @RequestParam String approved) {
        Booking newBooking = bookingStorage.responseBooking(requesterId, itemId, Boolean.parseBoolean(approved));
        log.info("Обновлено бронирование '{}'", newBooking);
        return newBooking;
    }

    @GetMapping("/{bookingId}")
    public ResponseBooking getBookingById(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                          @PathVariable("bookingId") int bookingId) {
        Optional<ResponseBooking> responseBooking = bookingStorage.getBookingById(bookingId, requesterId);
        log.info("Получено бронирование id '{}'", bookingId);
        return responseBooking.get();
    }

    @GetMapping
    public List<ResponseBooking> getListBookingsForUser(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                        @RequestParam(required = false) String state) {
        List<ResponseBooking> bookingList = bookingStorage.getListBookingForUser(requesterId, state);
        log.info("Получен запрос на лист бронирований для пользователя с id '{}'", requesterId);
        return bookingList;
    }

    @GetMapping("/owner")
    public List<ResponseBooking> getListBookingsForOwner(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        List<ResponseBooking> bookingList = bookingStorage.getListBookingForOwner(requesterId);
        log.info("Получен запрос на лист бронирований для пользователя с id '{}'", requesterId);
        return bookingList;
    }
}
