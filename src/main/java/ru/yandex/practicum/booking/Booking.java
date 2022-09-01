package ru.yandex.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "PUBLIC")
public class Booking {
    @Id
 //   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="booking_seq")
    @SequenceGenerator(
            name="booking_seq",
            sequenceName="booking_sequence",
            allocationSize=1)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    private User booker;

    @Column(name = "start_date")
    private Timestamp start;

    @Column(name = "end_date")
    private Timestamp end;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
