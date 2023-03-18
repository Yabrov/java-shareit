package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(
        name = "items",
        indexes = {
                @Index(name = "items_owner_id_idx", columnList = "owner_id"),
                @Index(name = "items_request_id_idx", columnList = "request_id")
        }
)
public class Item extends BaseEntity<Long> {

    public Item(String name,
                String description,
                Boolean available,
                User owner,
                ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }

    @Column(name = "name", nullable = false)
    private String name;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "text"
    )
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public Item withId(Long id) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setId(id);
        return item;
    }

    public Item withName(String name) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setName(name);
        return item;
    }

    public Item withDescription(String description) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setDescription(description);
        return item;
    }

    public Item withAvailable(Boolean available) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setAvailable(available);
        return item;
    }

    public Item withOwner(User owner) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setOwner(owner);
        return item;
    }

    public Item withRequest(ItemRequest request) {
        Item item = (Item) SerializationUtils.clone(this);
        item.setRequest(request);
        return item;
    }
}
