package ru.tbank.tenglish;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesGuestAndGeneratesId() {
        User saved = userRepository.save(User.guest("Dave", "dave@example.com"));

        assertThat(saved.id()).isNotNull();

        User found = userRepository.findById(saved.id()).orElseThrow();
        assertThat(found.name()).isEqualTo("Dave");
        assertThat(found.email()).isEqualTo("dave@example.com");
        assertThat(found.isGuest()).isTrue();
        assertThat(found.timezone()).isEqualTo("UTC");
        assertThat(found.createdAt()).isNotNull();
        assertThat(found.registeredAt()).isNull();
    }

    @Test
    void savesRegisteredUser() {
        User saved = userRepository.save(User.registered("Eve", "eve@example.com", "hash"));

        User found = userRepository.findById(saved.id()).orElseThrow();
        assertThat(found.isGuest()).isFalse();
        assertThat(found.passwordHash()).isEqualTo("hash");
        assertThat(found.registeredAt()).isNotNull();
    }

    @Test
    void rejectsDuplicateEmail() {
        userRepository.save(User.guest("Frank", "frank@example.com"));

        assertThatThrownBy(() -> userRepository.save(User.guest("Frank 2", "frank@example.com")))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsRegisteredUserWithoutPassword() {
        assertThatThrownBy(() -> userRepository.save(User.registered("Grace", "grace@example.com", null)))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void loadsSeedUsersFromDataSql() {
        assertThat(userRepository.findAll())
            .extracting(User::email)
            .contains("alice@example.com", "bob@example.com", "charlie@example.com");
    }
}
