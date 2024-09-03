package ru.evito.evito.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.evito.evito.dto.auth.RegisterDto;
import ru.evito.evito.dto.user.UserUpdateDto;
import ru.evito.evito.repositories.UserRepository;

import java.util.regex.Pattern;

@Service
public class UserValidationService {
    @Autowired private UserRepository userRepository;

    @Value("${admin.register.password}")
    private String adminPassword;

    public boolean isAdminPasswordValid(String password) {
        return adminPassword.equals(password);
    }

    public boolean isUsernameValid(String username) {
        if (username == null || username.isBlank())
            return false;
        var pattern = Pattern.compile("^[a-zA-Z0-9_,.\\-!?:;()]{3,60}$");
        return pattern.matcher(username).matches();
    }

    public boolean isRoleValid(String role) {
        if (role == null || role.isBlank())
            return false;
        var pattern = Pattern.compile("^(ROLE_ADMIN|ROLE_USER)$");
        return pattern.matcher(role).matches();
    }

    public boolean isPasswordValid(String password) {
        if (password == null || password.isBlank())
            return false;
        var pattern = Pattern.compile(".{8,60}$");
        return pattern.matcher(password).matches();
    }

    public boolean isNameValid(String name) {
        if (name == null || name.isBlank())
            return true;
        var pattern = Pattern.compile("^[a-zA-ZА-Яа-я_ .!?:;()]{1,60}$");
        return pattern.matcher(name).matches();
    }

    public boolean isEmailValid(String email) {
        if (email == null || email.isBlank())
            return false;
        var pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        return pattern.matcher(email).matches();
    }

    public boolean isTelValid(String tel) {
        if (tel == null || tel.isBlank())
            return false;
        var pattern = Pattern.compile("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$");
        return pattern.matcher(tel).matches();
    }

    public boolean isUserRegisterValid(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername()))
            return false;
        System.out.println(isUsernameValid(registerDto.getUsername()) + "-username");
        System.out.println(isRoleValid(registerDto.getRole()) + "-role");
        System.out.println(isPasswordValid(registerDto.getPassword()) + "-password");
        System.out.println(isNameValid(registerDto.getName()) + "-name");
        System.out.println(isNameValid(registerDto.getSurname()) + "-surname");
        System.out.println(isNameValid(registerDto.getPatronymic()) + "-patronymic");
        System.out.println(isEmailValid(registerDto.getEmail()) + "-email");
        System.out.println(isTelValid(registerDto.getTel()) + "-tel" + " " + registerDto.getTel());
        var isAdminPasswordValidVal = isAdminPasswordValid(registerDto.getAdminPassword());

        return isUsernameValid(registerDto.getUsername()) &&
                isPasswordValid(registerDto.getPassword()) &&
                isNameValid(registerDto.getName()) &&
                isNameValid(registerDto.getSurname()) &&
                isNameValid(registerDto.getPatronymic()) &&
                isEmailValid(registerDto.getEmail()) &&
                isTelValid(registerDto.getTel()) &&
                isRoleValid(registerDto.getRole()) &&
                (registerDto.getRole().equals("ROLE_USER") || isAdminPasswordValidVal);
    }

    public boolean isUserUpdateValid(UserUpdateDto userUpdateDto) {
        return isNameValid(userUpdateDto.getName()) &&
                isNameValid(userUpdateDto.getSurname()) &&
                isNameValid(userUpdateDto.getPatronymic()) &&
                isEmailValid(userUpdateDto.getEmail()) &&
                isTelValid(userUpdateDto.getTel());
    }
}
