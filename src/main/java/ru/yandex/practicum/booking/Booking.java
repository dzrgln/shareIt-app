package ru.yandex.practicum.booking;

import lombok.Data;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @ManyToOne
    private User booker;
    @Column(name = "start_date")
    private LocalDate start;
    @Column(name = "end_date")
    private LocalDate end;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
