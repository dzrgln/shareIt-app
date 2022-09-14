package ru.yandex.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "ORDER BY b.start desc ")
    List<Booking> getAllBookingForUser(int bookerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item = ?1 AND b.booker = ?2 AND b.status = ?3 AND b.start <= ?4 ")
    List<Booking> findBookingByItemBookerStatusStart(Item item, User booker, BookingStatus bookingStatus
            , LocalDateTime time);

    //Запросы для бронирований для любого пользователя
    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.status = ?2 " +
            "ORDER BY b.start asc ")
    List<Booking> getBookingForUserWithStatus(int bookerId, BookingStatus bookingStatus);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.end <= ?2 " +
            "ORDER BY b.start asc ")
    List<Booking> getPastBookingForUser(int bookerId, LocalDateTime timestamp);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.start > ?2 " +
            "ORDER BY b.start desc ")
    List<Booking> getFutureBookingForUser(int bookerId, LocalDateTime timestamp);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 AND b.start <= ?2 AND b.end > ?3 " +
            "ORDER BY b.start asc ")
    List<Booking> getCurrentBookingForUser(int bookerId, LocalDateTime timestamp1,
                                           LocalDateTime timestamp2);

    //Запросы для нахождения следующего и предыдущего бронирования
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner = ?1 AND b.item = ?2 AND b.end <= ?3 " +
            "ORDER BY b.end desc ")
    List<Booking> getLastBooking(User user, Item item, LocalDateTime timestamp);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner = ?1 AND b.item = ?2 AND b.start > ?3 " +
            "ORDER BY b.start asc")
    List<Booking> getNextBooking(User user, Item item, LocalDateTime timestamp);

    //Запросы для бронирований для собственника вещи
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 " +
            "ORDER BY b.start desc ")
    List<Booking> getAllBookingsForOwner(int bookerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.status = ?2 " +
            "ORDER BY b.start desc ")
    List<Booking> getBookingsForOwnerWithStatus(int bookerId, BookingStatus bookingStatus);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.start > ?2 " +
            "ORDER BY b.start desc")
    List<Booking> getFutureBookingsForOwner(int bookerId, LocalDateTime timestamp);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.end <= ?2 " +
            "ORDER BY b.start desc")
    List<Booking> getPastBookingsForOwner(int bookerId, LocalDateTime timestamp);

    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.start < ?2 AND b.end > ?3 " +
            "ORDER BY b.start asc ")
    List<Booking> getCurrentBookingsForOwner(int bookerId, LocalDateTime timestamp1, LocalDateTime timestamp2);

}
