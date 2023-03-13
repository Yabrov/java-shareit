package ru.practicum.shareit.config;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageBuilder {

    public Pageable build(Integer from, Integer size, Sort sort) {
        return PageRequest.of(from / size, size, sort);
    }
}
