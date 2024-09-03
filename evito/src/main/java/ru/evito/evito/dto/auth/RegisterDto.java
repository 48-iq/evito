package ru.evito.evito.dto.auth;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDto {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String patronymic;
    private String tel;
    private String role;
    private String adminPassword;
    private String email;
    private MultipartFile image;
}
