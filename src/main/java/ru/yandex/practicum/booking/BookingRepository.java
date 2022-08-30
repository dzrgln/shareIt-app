package ru.yandex.practicum.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByBooker_IdOrderByStartDesc(int bookerId);

        @Query(value = "SELECT bookings.id " +
                "    FROM bookings " +
                "left join items i on i.id = bookings.item_id " +
                "left join users u on u.id = i.owner_id " +
                "where owner_id = ? " +
                "order by start_date desc", nativeQuery = true)
    List<Booking> findByOwner(int ownerId);
//    @Query(value = "select Booking.id FROM Booking " +
//            "inner join Booking.item bi" +
//            "inner join Item.owner ow" +
//            "WHERE (ow.id) "
//
//    List<Booking> findByOwner(int bookerId);

//    List<Booking> findByOwner_id(int bookerId);

    List<Booking> findByItem(Item item);

    List<Booking> findByItemAndBookerAndStatusAndStartBefore(Item item, User booker, BookingStatus bookingStatus
            , Timestamp time);


    List<Booking> findByBooker_IdAndEndIsBeforeAndStartIsAfterOrderByStartDesc(int bookerId, Timestamp end,
                                                                               Timestamp start);

    List<Booking> findByBooker_IdAndEndIsBeforeOrderByStartDesc(int bookerId, Timestamp end);

    List<Booking> findByBooker_IdAndStartIsAfterOrderByStartDesc(int bookerId, Timestamp start);

    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);


}
