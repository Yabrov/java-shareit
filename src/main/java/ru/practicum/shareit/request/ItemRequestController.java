package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable Long requestId) {
        return itemRequestService.getItemRequest(requestId);
    }

    @PostMapping()
    public ItemRequestDto createItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getOwnItemRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests(
            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
            @RequestParam @Min(0) @NotNull Integer from,
            @RequestParam @Min(1) @NotNull Integer size) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
