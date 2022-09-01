package ru.yandex.practicum.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.user.User;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "PUBLIC")
public class Request {
    @Id
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="course_seq")
    @SequenceGenerator(
            name="course_seq",
            sequenceName="course_sequence",
            allocationSize=1)
    private int id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;

}
