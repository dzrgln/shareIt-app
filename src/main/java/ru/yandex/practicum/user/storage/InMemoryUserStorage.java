package ru.yandex.practicum.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.UserMapper;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private Set<String> emails = new HashSet<>();
    private int id = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User>  getUserById(Integer id) {
        Optional<User> optionalUser;
        if (users.containsKey(id)) {
            User user = users.get(id);
            optionalUser = Optional.of(user);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователя с id \"%s\" не существует.", id));
        }
        return optionalUser;
    }

    @Override
    public User create(User user) {
        if (!emails.contains(user.getEmail())) {
            id++;
            user.setId(id);
            users.put(user.getId(), user);
            emails.add(user.getEmail());
        } else {
            throw new ValidationException(
                    String.format("Пользователя с такой электронной почтой = \"%s\"уже существует.", user.getEmail()));
        }
        return user;
    }

    @Override
    public User update(int id, UserDto userDto) {
        if (id == 0) {
            throw new ValidationException("Введите id пользователя, которого необходимо обновить");
        }
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Указанного пользователя не существует");
        }
        if (emails.contains(userDto.getEmail())) {
            throw new ValidationException("Пользователь с такой электронной почтой уже существует");
        }
        emails.remove(getUserById(1).get().getEmail());
        User user = UserMapper.updateUser(userDto, getUserById(id).get());
        user.setId(id);
        users.put(id, user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public void delete(int id) {
        if (users.containsKey(id)) {
            emails.remove(users.get(id).getEmail());
            users.remove(id);
        } else {
            throw new ObjectNotFoundException(
                    String.format("Пользователя с указанным email существует \"%s\"не существует.", id));
        }
    }

    public boolean isUser(int id){
        return users.containsKey(id);
    }
}
