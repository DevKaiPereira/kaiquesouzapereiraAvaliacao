package com.kaiquesouzapereira.javaavaliacao.modules.users.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.InvalidCredentials;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Dto.AuthUserDto;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Service
public class AuthenticateUserService {
    @Value("${security.token.secret}")
    private String secret;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String execute(AuthUserDto authUserDto) {
        var userEntity = userRepository.findByEmail(authUserDto.getEmail()).orElseThrow(()-> new InvalidCredentials());
        if (!passwordEncoder.matches(authUserDto.getPassword(), userEntity.getPassword())) {
            throw new InvalidCredentials();
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var expiresIn = Instant.now().plus(Duration.ofDays(7));
        var token = JWT.create().withIssuer("Javaavaliacao")
                .withSubject(userEntity.getId().toString()).withClaim("name", userEntity.getUsername())
                .withClaim("roles", Arrays.asList(userEntity.getPosition().toString()))
                .withExpiresAt(expiresIn).sign(algorithm);

        return token;
    }
}
