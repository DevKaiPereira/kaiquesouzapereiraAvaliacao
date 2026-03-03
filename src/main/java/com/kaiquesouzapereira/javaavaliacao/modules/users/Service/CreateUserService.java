package com.kaiquesouzapereira.javaavaliacao.modules.users.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.UserAlreadyExists;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Entity.UserEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void execute(UserEntity userEntity) {
        userRepository.findByEmail(userEntity.getEmail()).ifPresent((user) -> {
            throw new UserAlreadyExists(userEntity.getEmail());
        });

        var password = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(password);
        this.userRepository.save(userEntity);
        return;
    }
}
