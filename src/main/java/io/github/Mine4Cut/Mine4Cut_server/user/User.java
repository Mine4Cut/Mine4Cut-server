package io.github.Mine4Cut.Mine4Cut_server.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 최대 10자리

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String email;

    private String socialInfo; // 일단 만들어두기만 함.
    private String interests; // 이후에 개발 예정
    private String frame; // 후에 프레임 엔티티 개발하면서 수정
}