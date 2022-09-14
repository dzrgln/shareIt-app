package ru.yandex.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(int bookerId);
    List<Booking> findByItemAndBookerAndStatusAndStartBefore(Item item, User booker, BookingStatus bookingStatus
            , Timestamp time);
    List<Booking> findByBooker_IdAndStatusOrderByStartAsc(int bookerId, BookingStatus bookingStatus);
    List<Booking> findByBooker_IdAndEndBeforeOrderByStartAsc(int bookerId, Timestamp timestamp);
    List<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(int bookerId, Timestamp timestamp);
    List<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartAsc(int bookerId, Timestamp timestamp1,
                                                                          Timestamp timestamp2);
    List<Booking> findByItem (Item item);
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner = ?1 AND b.item = ?2 AND b.end <= ?3 " +
            "ORDER BY b.end desc ")
    List<Booking> getLastBooking(User user, Item item, Timestamp timestamp);
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner = ?1 AND b.item = ?2 AND b.start > ?3 " +
            "ORDER BY b.start asc")
    List<Booking> getNextBooking(User user, Item item, Timestamp timestamp);
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
    List<Booking> getFutureBookingsForOwner(int bookerId, Timestamp timestamp);
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.end <= ?2 " +
            "ORDER BY b.start desc")
    List<Booking> getPastBookingsForOwner(int bookerId, Timestamp timestamp);
    @Query("SELECT b " +
            "FROM Booking b " +
            "JOIN Item i ON (b.item = i) " +
            "WHERE i.owner.id = ?1 AND b.start < ?2 AND b.end > ?3 " +
            "ORDER BY b.start asc ")
    List<Booking> getCurrentBookingsForOwner(int bookerId, Timestamp timestamp1, Timestamp timestamp2);

}
