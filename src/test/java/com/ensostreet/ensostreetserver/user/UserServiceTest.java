package com.ensostreet.ensostreetserver.user;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void shouldSaveUserIfEmailDoesNotExistYet() {
        String email = "abc@ensost.com";
        String name = "some name";
        String password = "12345";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        userService.registerUser(email, name, password);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User savedUser = argumentCaptor.getValue();
        assertThat(savedUser.email).isEqualTo(email);
        assertThat(savedUser.profileName).isEqualTo(name);
        assertThat(savedUser.failedLoginAttempts).isEqualTo(0);
        assertThat(savedUser.createdOn).isCloseTo(Instant.now(), new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS));
        assertThat(savedUser.password).isNotEqualTo(password).isNotBlank();
    }

    @Test
    void shouldThrowExceptionAndNotSaveUserIfEmailAlreadyExists() {
        String email = "abc@ensost.com";
        String name = "some name";
        String password = "12345";

        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(email, name, password))
                .isInstanceOf(UserAlreadyExists.class);

        verify(userRepository, times(0)).save(any());
    }
}
