package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
public class User {
    @With
    private Integer id;
    private String name;
    @Email
    private String email;
}
