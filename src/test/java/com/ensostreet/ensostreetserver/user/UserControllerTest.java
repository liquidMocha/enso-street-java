package com.ensostreet.ensostreetserver.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    String email = "some email";
    String password = "some pass";
    String name = "john doe";
    RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
            .email(email)
            .password(password)
            .profileName(name)
            .build();


    @Nested
    @DisplayName("create a new user")
    class createUser {
        @Test
        @DisplayName("should delegate to service with email, profile name, and password")
        void shouldHaveEndpointToCreateUser() throws Exception {
            mockMvc.perform(post("/users/createUser")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsBytes(registerUserRequest)))
                    .andExpect(status().isCreated());

            verify(userService).registerUser(email, name, password);
        }

        @Test
        @DisplayName("should not allow password that is less than 8 characters")
        void shouldReturn400BadRequestWhenPasswordIsLessThan8Characters() throws Exception {
            String email = "some email";
            String password = "1234567";
            String name = "john doe";
            RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                    .email(email)
                    .password(password)
                    .profileName(name)
                    .build();

            mockMvc.perform(post("/users/createUser")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsBytes(registerUserRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should not create a new user if the user already exists")
        void shouldReturn409ConflictWhenGettingUserAlreadyExistsException() throws Exception {
            doThrow(UserAlreadyExists.class).when(userService).registerUser(any(), any(), any());

            mockMvc.perform(post("/users/createUser")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsBytes(registerUserRequest)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("user login")
    class login {
        @Test
        void shouldHaveEndpointToLogin() throws Exception {
            LoginRequest loginRequest = LoginRequest.builder()
                    .email("some email")
                    .password("password")
                    .build();

            mockMvc.perform(post("/users/login")
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsBytes(loginRequest)))
                    .andExpect(status().isOk());
        }
    }
}
