package ru.evito.evito.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.evito.evito.dto.auth.LoginDto;
import ru.evito.evito.dto.auth.RegisterDto;
import ru.evito.evito.exceptions.UserNotFoundException;
import ru.evito.evito.security.JwtUtils;
import ru.evito.evito.services.UserService;
import ru.evito.evito.services.UserValidationService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserValidationService userValidationService;
    @Autowired private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        var token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword());
        try {
            authenticationManager.authenticate(token);
            var user = userService.getUserByUserName(loginDto.getUsername());
            var jwtToken = jwtUtils.generate(user);
            return ResponseEntity.ok(jwtToken);

        } catch (BadCredentialsException exc) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
        } catch (UsernameNotFoundException | UserNotFoundException exc) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@ModelAttribute RegisterDto registerDto) {
        if (!userValidationService.isUserRegisterValid(registerDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data");
        }
        var user = userService.registerUser(registerDto);
        var jwtToken = jwtUtils.generate(user);
        return ResponseEntity.ok(jwtToken);
    }
}
