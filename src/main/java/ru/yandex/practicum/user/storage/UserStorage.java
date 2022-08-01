package ru.yandex.practicum.user.storage;

import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getUsers();

    Optional<User> getUserById(Integer id);

    User create(User user);

    User update (int id, UserDto userDto);

    void delete (int id);

    boolean isUser(int id);
}
