INSERT INTO users(name, email) VALUES ('test_name_1', 'email_1@test.com');
INSERT INTO users(name, email) VALUES ('test_name_2', 'email_2@test.com');

INSERT INTO items(name, description, is_available, owner_id, request_id)
VALUES ('test_item_name', 'test_item_description', true, 1, null);
INSERT INTO items(name, description, is_available, owner_id, request_id)
VALUES ('test_item_name_2', 'test_item_description_2', true, 1, null);

INSERT INTO bookings(start_date, end_date, status, booker_id, item_id)
VALUES ('2023-01-01 11:00:00'::timestamp, '2023-01-01 12:00:00'::timestamp, 'APPROVED', 2, 1);
INSERT INTO bookings(start_date, end_date, status, booker_id, item_id)
VALUES ('2023-02-01 11:00:00'::timestamp, '2043-01-01 09:00:00'::timestamp, 'APPROVED', 2, 1);
INSERT INTO bookings(start_date, end_date, status, booker_id, item_id)
VALUES ('2043-01-01 10:00:00'::timestamp, '2043-01-01 11:00:00'::timestamp, 'APPROVED', 2, 1);
INSERT INTO bookings(start_date, end_date, status, booker_id, item_id)
VALUES ('2043-01-01 11:00:00'::timestamp, '2043-01-01 12:00:00'::timestamp, 'WAITING', 2, 1);
INSERT INTO bookings(start_date, end_date, status, booker_id, item_id)
VALUES ('2043-01-01 12:00:00'::timestamp, '2043-01-01 13:00:00'::timestamp, 'REJECTED', 2, 1);

INSERT INTO comments(text, author_id, item_id, created)
VALUES ('test_comment', 2, 1, '2023-01-01 13:00:00'::timestamp);