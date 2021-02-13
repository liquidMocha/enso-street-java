package com.ensostreet.ensostreetserver.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

    String existingEmail = "email@ensost.com";
    User existingUser;

    @BeforeEach
    void setup() {
        existingUser = User.builder().email(existingEmail).build();
        testEntityManager.persist(existingUser);
        testEntityManager.flush();
    }

    @Nested
    class userExists {
        @Test
        void shouldReturnTrueIfUserWithSpecifiedEmailAlreadyExists() {
            boolean userExists = userRepository.existsByEmail(existingEmail);

            assertThat(userExists).isTrue();
        }

        @Test
        void shouldReturnFalseIfUserWithSpecifiedEmailDoesNotExist() {
            boolean userExists = userRepository.existsByEmail("different@email");

            assertThat(userExists).isFalse();
        }
    }

    @Test
    void shouldReturnUserByEmail() {
        User actual = userRepository.getUserByEmail(existingEmail);

        assertThat(actual.email).isEqualTo(existingUser.email);
    }

    @Test
    void shouldReturnNullWhenUserIsNotFound() {
        assertThat(userRepository.getUserByEmail("garbage")).isNull();
    }
}
