package ru.evito.evito.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.evito.evito.dto.auth.RegisterDto;
import ru.evito.evito.dto.user.UserDto;
import ru.evito.evito.dto.user.UserUpdateDto;
import ru.evito.evito.exceptions.UserNotFoundException;
import ru.evito.evito.models.Role;
import ru.evito.evito.models.User;
import ru.evito.evito.repositories.UserRepository;

import java.io.IOException;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ImageService imageService;

    @Transactional
    public UserDto registerUser(RegisterDto registerDto) {
        var user = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .name(registerDto.getName())
                .surname(registerDto.getSurname())
                .patronymic(registerDto.getPatronymic())
                .tel(registerDto.getTel())
                .role(Role.valueOf(registerDto.getRole()))
                .email(registerDto.getEmail())
                .build();
        if (registerDto.getImage() != null) {
            try {
                user.setImage(imageService.saveImage(registerDto.getImage().getBytes()));
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
        var savedUser = userRepository.save(user);

        return UserDto.fromUser(savedUser);
    }

    public UserDto getUserByUserName(String username) {
        var user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserDto.fromUser(user);
    }

    @Transactional
    public UserDto updateUser(String username, UserUpdateDto userUpdateDto) {
        var user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setName(userUpdateDto.getName());
        user.setSurname(userUpdateDto.getSurname());
        user.setPatronymic(userUpdateDto.getPatronymic());
        user.setTel(userUpdateDto.getTel());
        user.setEmail(userUpdateDto.getEmail());
        System.out.println(userUpdateDto.getImage());
        System.out.println(userUpdateDto);
        if (userUpdateDto.getImage() != null) {
            try {
                System.out.println("yes");
                if (user.getImage() != null) {
                    System.out.println("update");
                    imageService.rewriteImage(user.getImage(), userUpdateDto.getImage().getBytes());
                } else {
                    System.out.println("save");
                    user.setImage(imageService.saveImage(userUpdateDto.getImage().getBytes()));

                }
            } catch (IOException exc) {
                System.out.println("exception");
                throw new RuntimeException(exc);
            }
        }

        var savedUser = userRepository.save(user);
        return UserDto.fromUser(savedUser);
    }

    @Transactional
    public void deleteUser(String username) {

        userRepository.deleteUserByUsername(username);
    }
}
