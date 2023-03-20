package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.config.IdReducer;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepositoryImpl;
import ru.practicum.shareit.user.User;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Rollback
@DataJpaTest
@Transactional(readOnly = true)
@Sql(scripts = "classpath:request_init.sql")
@Import(value = {ItemRequestRepositoryImpl.class, IdReducer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestRepositoryTest {

    private final ItemRequestRepository requestRepository;

    private final IdReducer idReducer;

    private final Long expectedRequestId = 1L;

    private final Long expectedOwnerId = 1L;

    private final Long expectedRequestorId = 2L;

    @BeforeEach
    public void setUp() throws SQLException {
        idReducer.resetAutoIncrementColumns("items");
        idReducer.resetAutoIncrementColumns("users");
        idReducer.resetAutoIncrementColumns("requests");
    }

    @Test
    @DisplayName("Find item request by id test")
    void findItemRequestByIdTest() throws Exception {
        assertThat(requestRepository.getItemRequest(expectedRequestId)).isNotNull();
    }

    @Test
    @DisplayName("Find item requests by requestor test")
    void findAllItemRequestByRequestorTest() throws Exception {
        User requestor = new User().withId(expectedRequestorId);
        Sort requestsSort = Sort.by(Sort.Direction.DESC, "created");
        assertThat(requestRepository.getItemRequestsByOwner(requestor, requestsSort)).asList().hasSize(1);
    }

    @Test
    @DisplayName("Find item requests test")
    void findAllItemRequestTest() throws Exception {
        User requestor = new User().withId(expectedRequestorId);
        assertThat(requestRepository.getAllItemRequests(requestor)).asList().isEmpty();
    }
}
