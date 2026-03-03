package com.kaiquesouzapereira.javaavaliacao.modules.users.Controller;

import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.UserAlreadyExists;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Dto.CreateUserDto;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Entity.UserEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Service.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for VEXYN file management system")
public class UserController {

    @Autowired
    private CreateUserService createUserService;
    @Operation(summary = "Create User")
    @PostMapping("")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document successfully modified."),
            @ApiResponse(responseCode = "409", description = "The user already exists.")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserDto createUserDto) {
        try {
            var userEntity = UserEntity.builder().username(createUserDto.getUsername()).password(createUserDto.getPassword()).email(createUserDto.getEmail()).position(UserEntity.fromValue(createUserDto.getPosition())).build();
            this.createUserService.execute(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserAlreadyExists e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }   catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
