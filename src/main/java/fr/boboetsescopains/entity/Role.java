package fr.boboetsescopains.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 20)
    private RoleName name;

    @Column(length = 100)
    private String description;

    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    private Set<User> users = new HashSet<>();

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
