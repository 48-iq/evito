package ru.evito.evito.dto.user;

import lombok.*;
import ru.evito.evito.models.User;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String patronymic;
    private String tel;
    private String email;
    private String image;
    private String role;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .tel(user.getTel())
                .email(user.getEmail())
                .image(user.getImage())
                .role(user.getRole().name())
                .build();
    }
}
