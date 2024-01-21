package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, length = 250)
    @Size(min = 2, max = 250)
    private String name;
    @Column(name = "email", nullable = false, unique = true, length = 254)
    @Size(min = 6, max = 254)
    private String email;
}
