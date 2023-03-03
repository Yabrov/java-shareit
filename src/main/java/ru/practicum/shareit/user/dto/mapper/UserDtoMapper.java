package ru.practicum.shareit.user.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserDtoMapper implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto source) {
        User user = new User();
        user.setId(null);
        user.setName(source.getName());
        user.setEmail(source.getEmail());
        return user;
    }
}
