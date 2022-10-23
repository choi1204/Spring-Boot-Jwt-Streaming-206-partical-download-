package com.test.genesis.domain.user;

import com.test.genesis.domain.file.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.matcher.FilterableList;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "userEntity")
    private List<FileEntity> fileList = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private boolean isDelete;

    private LocalDateTime deletedAt;

    @Builder
    private UserEntity(Long id, String email, String name, String password, String phoneNumber, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.isDelete = false;
    }

    public void encrypt(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void update(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void delete() {
        this.isDelete = true;
        this.deletedAt = LocalDateTime.now();
    }
}
