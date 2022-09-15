package ru.yandex.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.booking.DTO.RequestBooking;
import ru.yandex.practicum.booking.DTO.ResponseBooking;
import ru.yandex.practicum.booking.storage.BookingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingsMapper bookingsMapper;

    @PostMapping
    public ResponseBooking requestBooking(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                          @RequestBody RequestBooking requestBooking) {
        Booking newBooking = bookingService.requestBooking(requesterId, requestBooking);
        log.info("Создано бронирование '{}'", newBooking);

        return bookingsMapper.bookingToResponseBooking(newBooking);
    }


    @PatchMapping("/{itemId}")
    public ResponseBooking responseBooking(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                   @PathVariable("itemId") int itemId,
                                   @RequestParam String approved) {
        Booking newBooking = bookingService.responseBooking(requesterId, itemId, Boolean.parseBoolean(approved));
        log.info("Обновлено бронирование '{}'", newBooking);
        return bookingsMapper.bookingToResponseBooking(newBooking);
    }

    @GetMapping("/{bookingId}")
    public ResponseBooking getBookingById(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                          @PathVariable("bookingId") int bookingId) {
        Optional<ResponseBooking> responseBooking = bookingService.getBookingById(bookingId, requesterId);
        log.info("Получено бронирование id '{}'", bookingId);
        return responseBooking.get();
    }

    @GetMapping
    public List<ResponseBooking> getListBookingsForUser(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                        @RequestParam(required = false) String state) {
        List<ResponseBooking> bookingList = bookingService.getListBookingForUser(requesterId, state);
        log.info("Получен запрос на лист бронирований для пользователя с id '{}'", requesterId);
        return bookingList;
    }

    @GetMapping("/owner")
    public List<ResponseBooking> getListBookingsForOwner(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                         @RequestParam(required = false) String state) {
        List<ResponseBooking> bookingList = bookingService.getListBookingForOwner(requesterId, state);
        log.info("Получен запрос на лист бронирований для пользователя с id '{}'", requesterId);
        return bookingList;
    }
}
