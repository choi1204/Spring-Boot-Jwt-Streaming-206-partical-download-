package com.test.genesis.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
@Where(clause = "is_delete=false")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "boolean default false")
    private boolean isDelete;

    private LocalDateTime deletedAt;

    private UserEntity(String email, String name, String phoneNumber, Role role) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
        isDelete = false;
    }

    public static UserEntity createUser(String email, String name, String phoneNumber) {
        return new UserEntity(email,name, phoneNumber, Role.USER);
    }

    public static UserEntity createAdmin(String email, String name, String phoneNumber) {
        return new UserEntity(email,name, phoneNumber, Role.ADMIN);
    }

    public void update(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void delete() {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }
}
