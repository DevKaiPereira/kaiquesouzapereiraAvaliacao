package com.kaiquesouzapereira.javaavaliacao.modules.users.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.InvalidCredentials;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Dto.AuthUserDto;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Entity.UserEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUserServiceTest {

    @InjectMocks
    private AuthenticateUserService authenticateUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticateUserService, "secret", "test_secret_123");
    }

    @Test
    @DisplayName("Should return a token when credentials are valid")
    void should_authenticate_user_and_return_token() {

        var  email = "test@example.com";
        var password = "password123";
        var authDto = new AuthUserDto(email, password);

        var user = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encoded_password")
                .position(UserEntity.Position.ADMIN)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        var token = authenticateUserService.execute(authDto);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();

        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should throw InvalidCredentials when user is not found")
    void should_throw_exception_when_user_not_found() {

        var authDto = new AuthUserDto("wrong@email.com", "any_password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentials.class, () -> {
            authenticateUserService.execute(authDto);
        });
    }

    @Test
    @DisplayName("Should throw InvalidCredentials when password does not match")
    void should_throw_exception_when_password_is_incorrect() {

        var email = "test@example.com";
        var authDto = new AuthUserDto(email, "wrong_password");
        var user = UserEntity.builder().email(email).password("encoded_pass").build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentials.class, () -> {
            authenticateUserService.execute(authDto);
        });
    }
}