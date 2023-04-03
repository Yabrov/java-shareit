package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public interface JpaCommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByItem_IdOrderByCreatedDesc(Long itemId);

    Integer countAllByItem_IdAndAuthor_Id(Long itemId, Long authorId);
}
