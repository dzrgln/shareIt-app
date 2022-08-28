package ru.yandex.practicum.user;

import lombok.*;
import ru.yandex.practicum.request.Request;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Email
    @NotNull
    @Column(name = "email")
    private String email;

//    @OneToMany(mappedBy = "owner")
//    private List<Item> items;
    @OneToMany(mappedBy = "requester")
    private List<Request> requestItems;


    public User(int id, String name, @NonNull String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


}
