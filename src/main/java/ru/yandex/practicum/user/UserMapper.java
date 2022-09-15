package ru.yandex.practicum.user;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }

    public static User updateUser (UserDto userDto, User user){
        return new User(
                user.getId(),
                userDto.getName() != null ? userDto.getName() : user.getName(),
                userDto.getEmail() != null ? userDto.getEmail() : user.getEmail()
        );
    }
}
