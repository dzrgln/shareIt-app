package ru.yandex.practicum.item;

import lombok.*;
import ru.yandex.practicum.request.Request;
import ru.yandex.practicum.user.User;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
//@Builder
//@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @NotNull
    @AssertTrue
    private boolean available;
//    @ManyToOne
//    @JoinColumn(name = "requester_id", referencedColumnName = "id")
//    private Request request = null;
}
