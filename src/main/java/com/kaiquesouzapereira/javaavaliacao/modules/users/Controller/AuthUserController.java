package com.kaiquesouzapereira.javaavaliacao.modules.users.Controller;


import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.InvalidCredentials;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Dto.AuthUserDto;
import com.kaiquesouzapereira.javaavaliacao.modules.users.Service.AuthenticateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class AuthUserController {
    @Autowired
    private AuthenticateUserService authenticateUserService;
    @Operation(summary = "Authenticate User")
    @PostMapping("/auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successfully generated."),
            @ApiResponse(responseCode = "401", description = "Unauthorized user.")
    })
    public ResponseEntity<Object> authenticateUser(@RequestBody AuthUserDto authUserDto) {
        try{
            var result = authenticateUserService.execute(authUserDto);
            return ResponseEntity.ok(result);
        } catch (InvalidCredentials e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }


}
