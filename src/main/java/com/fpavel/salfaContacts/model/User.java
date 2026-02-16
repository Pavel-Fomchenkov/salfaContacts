package com.fpavel.salfaContacts.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    private String passwordEncrypted;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public User() {
    }

    public User(Long id, String login, String passwordEncrypted, Role role) {
        this.id = id;
        this.login = login;
        this.passwordEncrypted = passwordEncrypted;
        this.role = role;
    }

    public User(Long id, String login, Role role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public User(String login, String passwordEncrypted, Role role) {
        this.login = login;
        this.passwordEncrypted = passwordEncrypted;
        this.role = role;
    }

    public User(Long id, String passwordEncrypted) {
        this.id = id;
        this.passwordEncrypted = passwordEncrypted;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    public Role getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordEncrypted(String passwordEncrypted) {
        this.passwordEncrypted = passwordEncrypted;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        USER,
        SUPERUSER,
        ADMIN
    }

}
