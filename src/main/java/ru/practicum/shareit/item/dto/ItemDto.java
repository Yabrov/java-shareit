package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
}
