package ru.practicum.shareit.user;

import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity<Long> {

    private String name;
    private String email;
    private List<Item> items = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private List<ItemRequest> itemRequests = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @Column(
            name = "email",
            length = 512,
            unique = true,
            nullable = false
    )
    public String getEmail() {
        return email;
    }

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    public List<Item> getItems() {
        return items;
    }

    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY)
    public List<Booking> getBookings() {
        return bookings;
    }

    @OneToMany(mappedBy = "requestor", fetch = FetchType.LAZY)
    public List<ItemRequest> getItemRequests() {
        return itemRequests;
    }

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    public List<Comment> getComments() {
        return comments;
    }
}
