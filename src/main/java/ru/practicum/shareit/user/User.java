package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity<Long> {

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

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

    public User withId(Long id) {
        User user = (User) SerializationUtils.clone(this);
        user.setId(id);
        return user;
    }

    public User withName(String name) {
        User user = (User) SerializationUtils.clone(this);
        user.setName(name);
        return user;
    }

    public User withEmail(String email) {
        User user = (User) SerializationUtils.clone(this);
        user.setEmail(email);
        return user;
    }
}
