package ru.practicum.shareit.config;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;

@Component
public class PageBuilder {

    public Pageable build(@NonNull Integer from, @NonNull Integer size, Sort sort) {
        if (from < 0 || size < 1) {
            throw new InvalidPaginationParamsException(from, size);
        }
        return sort == null
                ? PageRequest.of(from / size, size)
                : PageRequest.of(from / size, size, sort);
    }
}
