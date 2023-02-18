package ru.practicum.shareit.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    @With
    private Integer id;

    @With
    private String name;

    @With
    private String email;
}
