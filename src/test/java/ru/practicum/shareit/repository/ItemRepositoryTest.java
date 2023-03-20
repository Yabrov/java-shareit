package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.config.IdReducer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DatabaseItemRepositoryImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Rollback
@DataJpaTest
@Transactional(readOnly = true)
@Sql(scripts = "classpath:item_init.sql")
@Import(value = {DatabaseItemRepositoryImpl.class, IdReducer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRepositoryTest {

    private final ItemRepository itemRepository;

    private final IdReducer idReducer;

    private final Long expectedOwnerId = 1L;

    private final Long expectedItemId = 1L;

    private final User owner = new User(
            "test_name_1",
            "email_1@test.com"
    ).withId(expectedOwnerId);

    private final Item item = new Item(
            "test_item_name",
            "test_item_description",
            true,
            owner,
            null
    ).withId(expectedItemId);

    @BeforeEach
    public void setUp() throws SQLException {
        idReducer.resetAutoIncrementColumns("items");
        idReducer.resetAutoIncrementColumns("users");
        idReducer.resetAutoIncrementColumns("bookings");
        idReducer.resetAutoIncrementColumns("comments");
    }

    @Test
    @DisplayName("Find item by id test")
    void findItemByIdTest() throws Exception {
        assertThat(itemRepository.findItemById(expectedItemId)).isEqualTo(item);
    }

    @Test
    @DisplayName("Get last booking test")
    void getLastBookingTest() throws Exception {
        Long expectedLastBookingId = 2L;
        assertThat(itemRepository.getLastBookingByItemId(expectedItemId).getId())
                .isEqualTo(expectedLastBookingId);
    }

    @Test
    @DisplayName("Get next booking test")
    void getNextBookingTest() throws Exception {
        Long expectedNextBookingId = 3L;
        assertThat(itemRepository.getNextBookingByItemId(expectedItemId).getId())
                .isEqualTo(expectedNextBookingId);
    }

    @Test
    @DisplayName("Is user real booker of item valid test")
    void isUserRealBookerOfItemValidTest() throws Exception {
        Long expectedBookerId = 2L;
        assertThat(itemRepository.isUserRealBookerOfItem(expectedItemId, expectedBookerId)).isTrue();
    }

    @Test
    @DisplayName("Is user real booker of item fail test")
    void isUserRealBookerOfItemFailTest() throws Exception {
        Long wrongUserId = 99L;
        assertThat(itemRepository.isUserRealBookerOfItem(expectedItemId, wrongUserId)).isFalse();
    }

    @Test
    @DisplayName("Is user commentator of item valid test")
    void isUserCommentatorOfItemValidTest() throws Exception {
        Long expectedBookerId = 2L;
        assertThat(itemRepository.isUserCommentatorOfItem(expectedItemId, expectedBookerId)).isTrue();
    }

    @Test
    @DisplayName("Is user real commentator of item fail test")
    void isUserCommentatorOfItemFailTest() throws Exception {
        Long wrongUserId = 99L;
        assertThat(itemRepository.isUserCommentatorOfItem(expectedItemId, wrongUserId)).isFalse();
    }

    @ParameterizedTest
    @DisplayName("Search item test")
    @ValueSource(strings = {"descr", "Descri", "eScr", "st"})
    void searchItemTest(String text) throws Exception {
        assertThat(itemRepository.searchItem(text)).asList().contains(item);
    }
}
