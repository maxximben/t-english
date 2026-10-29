drop table if exists users;

create table users (
	id bigint generated always as identity primary key,
	name varchar(30) not null,
	email text unique,
	password_hash text,
	is_guest boolean not null default true,
	avatar_key text,
	timezone text not null default 'UTC',
	created_at timestamptz not null default now(),
	registered_at timestamptz,
	last_active_at timestamptz not null default now(),
	constraint users_registered_has_credentials CHECK (
		is_guest or (email is not null and password_hash is not null and registered_at is not null)
    )
);
