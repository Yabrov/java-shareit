package ru.practicum.shareit.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

    private Integer id;
    private String name;
    private String email;
}
