package ru.evito.evito.dto.user;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserUpdateDto {
    private String name;
    private String surname;
    private String patronymic;
    private String tel;
    private String email;
    private MultipartFile image;
}
