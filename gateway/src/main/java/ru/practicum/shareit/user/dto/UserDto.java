package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NotNull
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto implements Serializable {

    private static final Long serialVersionUID = 2L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @Email
    @NotNull
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

    public UserDto withEmail(String email) {
        UserDto userDto = (UserDto) SerializationUtils.clone(this);
        userDto.setEmail(email);
        return userDto;
    }
}
