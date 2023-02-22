package ru.practicum.shareit.user.dto.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        return new UserDto(
                source.getId(),
                source.getName(),
                source.getEmail()
        );
    }
}
