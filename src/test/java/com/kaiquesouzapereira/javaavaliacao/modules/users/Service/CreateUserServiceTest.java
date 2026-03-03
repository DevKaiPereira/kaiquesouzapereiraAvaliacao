package com.kaiquesouzapereira.javaavaliacao.modules.users.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.UserAlreadyExists;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Entity.UserEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {

    @InjectMocks
    private CreateUserService createUserService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should be able to create a new user successfully")
    public void should_be_able_to_create_a_new_user() {

        var email = "novo_usuario@gmail.com";
        var userEntity = UserEntity.builder()
                .username("Novo Usuario")
                .email(email)
                .password("senha123")
                .position(UserEntity.Position.ADMIN)
                .build();

        when(passwordEncoder.encode(any())).thenReturn("password_encoded");
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        createUserService.execute(userEntity);

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("should be able check if the email already exists.")
    public void should_be_able_user_email_already_exists() {

        var email = "kaique@gmail.com";
        var userEntity = UserEntity.builder()
                .username("Kaique")
                .email(email)
                .password("123456")
                .position(UserEntity.Position.ADMIN)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExists.class, () -> {
            createUserService.execute(userEntity);
        });
    }
}