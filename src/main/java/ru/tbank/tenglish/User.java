package ru.tbank.tenglish;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record User(
    @Id Long id,
    String name,
    String email,
    String passwordHash,
    boolean isGuest,
    String avatarKey,
    String timezone,
    Instant createdAt,
    Instant registeredAt,
    Instant lastActiveAt
) {

    public static User guest(String name, String email) {
        Instant now = Instant.now();
        return new User(null, name, email, null, true, null, "UTC", now, null, now);
    }

    public static User registered(String name, String email, String passwordHash) {
        Instant now = Instant.now();
        return new User(null, name, email, passwordHash, false, null, "UTC", now, now, now);
    }
}
