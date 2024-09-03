package ru.evito.evito.dto.auth;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    private String username;
    private String password;
}
