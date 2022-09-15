package ru.yandex.practicum.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.storage.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userStorage;

    public UserController(UserService userStorage) {
        this.userStorage = userStorage;
    }


    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET-запрос к эндпоинту /users");
        return userStorage.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") int userId){
        return userStorage.getUserById(userId).get();
    }

    @PostMapping
    public User create(@Validated @RequestBody User user) {
        User newUser = userStorage.create(user);
        log.info("Создан объект '{}'", newUser);
        return newUser;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable("userId") int userId, @Validated @RequestBody UserDto userDto) {
        log.info("Обновлен объект '{}'", userDto);
        return userStorage.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") int userId){
        log.info("Удален пользователь '{}'", userStorage.getUserById(userId));
        userStorage.delete(userId);
    }
}

