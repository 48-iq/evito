package ru.evito.evito.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;
    private Role role;

    private String name;
    private String surname;
    private String patronymic;
    private String tel;
    private String email;
    private String image;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Product> products;
}
