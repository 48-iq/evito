package ru.evito.evito.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.evito.evito.dto.user.UserDto;
import ru.evito.evito.dto.user.UserUpdateDto;
import ru.evito.evito.exceptions.UserNotFoundException;
import ru.evito.evito.services.UserService;
import ru.evito.evito.services.UserValidationService;


@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService userService;
    @Autowired private UserValidationService userValidationService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        try {
            var user = userService.getUserByUserName(username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException exc) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("authentication.principal.username == #username")
    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUserByUsername(@PathVariable String username,
                                                        @ModelAttribute UserUpdateDto userUpdateDto) {

        if (!userValidationService.isUserUpdateValid(userUpdateDto)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            var user = userService.updateUser(username, userUpdateDto);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException exc) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("authentication.principal.username == #username")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException exc) {
            return ResponseEntity.notFound().build();
        }
    }

}
