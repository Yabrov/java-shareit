package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    public UserDto withId(Long id) {
        UserDto userDto = (UserDto) SerializationUtils.clone(this);
        userDto.setId(id);
        return userDto;
    }

    public UserDto withName(String name) {
        UserDto userDto = (UserDto) SerializationUtils.clone(this);
        userDto.setName(name);
        return userDto;
    }
}
