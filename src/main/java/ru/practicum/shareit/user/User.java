package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@With
@Entity
@Getter
@Setter
@SuperBuilder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(
            name = "email",
            length = 512,
            unique = true,
            nullable = false
    )
    private String email;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "requestor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemRequest> itemRequests = new ArrayList<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
}
