package ru.yandex.practicum.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.user.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "public")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;

}
