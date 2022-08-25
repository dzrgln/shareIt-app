package ru.yandex.practicum.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.UserMapper;
import ru.yandex.practicum.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
//@Qualifier("DbUser")
@RequiredArgsConstructor
public class DbUserStorage implements UserStorage{

    private final UserRepository repository;

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        Optional<User> optionalUser;
        if(repository.existsById(id)){
            optionalUser = repository.findById(id);
        } else {
            throw new ObjectNotFoundException(String.format("Пользователя с id \"%s\" не существует.", id));
        }
        return optionalUser;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public User update(int id, UserDto userDto) {
        validateUpdate(id, userDto);
        return repository.save(UserMapper.updateUser(userDto, getUserById(id).get()));
    }

    private void validateUpdate(int id, UserDto userDto) {
        if (id == 0) {
            throw new ValidationException("Введите id пользователя, которого необходимо обновить");
        }
        if (!repository.existsById(id)) {
            throw new ObjectNotFoundException("Указанного пользователя не существует");
        }
    }

    @Override
    public void delete(int id) {
        repository.deleteById(id);

    }

    @Override
    public boolean isUser(int id) {
        return repository.existsById(id);
    }
}
