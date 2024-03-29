package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.shareit.config.BaseEntity;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Table(
        name = "bookings",
        indexes = {
                @Index(name = "booking_item_id_idx", columnList = "item_id"),
                @Index(name = "booking_booker_id_idx", columnList = "booker_id")
        }
)
public class Booking extends BaseEntity<Long> {

    public Booking(Long id,
                   LocalDateTime start,
                   LocalDateTime end,
                   Item item,
                   User booker,
                   BookingStatus status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    @Column(
            name = "start_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    private LocalDateTime start;

    @Column(
            name = "end_date",
            nullable = false,
            columnDefinition = "timestamp"
    )
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Column(
            name = "status",
            length = 10,
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING;

    public Booking withId(Long id) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setId(id);
        return booking;
    }

    public Booking withStart(LocalDateTime start) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setStart(start);
        return booking;
    }

    public Booking withEnd(LocalDateTime end) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setStart(end);
        return booking;
    }

    public Booking withItem(Item item) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setItem(item);
        return booking;
    }

    public Booking withBooker(User booker) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setBooker(booker);
        return booking;
    }

    public Booking withStatus(BookingStatus status) {
        Booking booking = (Booking) SerializationUtils.clone(this);
        booking.setStatus(status);
        return booking;
    }
}
