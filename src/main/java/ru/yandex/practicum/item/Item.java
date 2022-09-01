package ru.yandex.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.booking.Booking;
import ru.yandex.practicum.comment.Comment;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "PUBLIC")
public class Item {
    @Id
//    @GeneratedValue
            //(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="course_seq")
    @SequenceGenerator(
            name="course_seq",
            sequenceName="course_sequence",
            allocationSize=1
    )
    private int id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull()
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany
    @JoinColumn(name = "item")
    private List<Booking> bookingList;

    @OneToMany
    @JoinColumn(name = "item")
    private List<Comment> commentsList;

    @NotNull
    private Boolean available;


//    @ManyToOne
//    @JoinColumn(name = "requester_id", referencedColumnName = "id")
//    private Request request = null;
}
