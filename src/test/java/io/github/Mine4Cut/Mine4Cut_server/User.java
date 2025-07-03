package io.github.Mine4Cut.Mine4Cut_server;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;

    public User() {}

    public User(String testuser, String mail) {
        this.name = testuser;
        this.email = mail;
    }


    // 생성자, getter/setter 생략
}