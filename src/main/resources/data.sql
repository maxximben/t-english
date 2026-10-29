-- Тестовые пользователи. Пароль у всех зарегистрированных: password (BCrypt)
insert into users (name, email, password_hash, is_guest, avatar_key, timezone, created_at, registered_at, last_active_at)
values
	('Alice', 'alice@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BW', false, null, 'Europe/Moscow', now() - interval '30 days', now() - interval '30 days', now() - interval '1 hour'),
	('Bob', 'bob@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BW', false, 'avatars/bob.png', 'Europe/London', now() - interval '14 days', now() - interval '13 days', now() - interval '2 days'),
	('Charlie', 'charlie@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BW', false, null, 'Asia/Yekaterinburg', now() - interval '7 days', now() - interval '7 days', now()),
	('Guest', null, null, true, null, 'UTC', now() - interval '1 day', null, now() - interval '3 hours'),
	('Guest', null, null, true, null, 'Europe/Moscow', now(), null, now());
