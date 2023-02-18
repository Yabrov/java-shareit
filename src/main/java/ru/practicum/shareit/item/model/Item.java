package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Item {

    @With
    private Integer id;

    @With
    private String name;

    @With
    private String description;

    @With
    private Boolean available;

    @With
    private User owner;

    @With
    private ItemRequest request;
}
