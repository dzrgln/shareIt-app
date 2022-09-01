package ru.yandex.practicum.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(int bookerId);

    List<Booking> findByItemAndBookerAndEndBeforeOrderByEndDesc(Item item, User booker, Timestamp timestamp);
    List<Booking> findByItemAndBookerAndStartAfterOrderByStartAsc(Item item, User booker, Timestamp timestamp);

    List<Booking> findByItemAndBookerAndStatusAndStartBefore(Item item, User booker, BookingStatus bookingStatus
            , Timestamp time);

    List<Booking> findByBookerAndItem(User booker, Item item);
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

}
