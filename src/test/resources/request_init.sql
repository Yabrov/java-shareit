INSERT INTO users(name, email) VALUES ('test_name_1', 'email_1@test.com');
INSERT INTO users(name, email) VALUES ('test_name_2', 'email_2@test.com');

INSERT INTO requests(description, requestor_id, created)
VALUES ('test_description', 2, '2023-01-01 10:00:00'::timestamp);

INSERT INTO items(name, description, is_available, owner_id, request_id)
VALUES ('test_item_name', 'test_item_description', true, 1, 1);

