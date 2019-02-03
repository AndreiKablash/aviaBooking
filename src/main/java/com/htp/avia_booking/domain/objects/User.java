package com.htp.avia_booking.domain.objects;

import com.htp.avia_booking.domain.Entity;
import com.htp.avia_booking.domain.source.objects.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity {
    private String name;
    private String surname;
    private String documentId;
    private String email;
    private String login;
    private String password;
    private String phone;
    private List<Role> role = new ArrayList<>();

    public User() {
        super();
    }

    public User(long id, String login, String password) {
        super(id);
        this.login = login;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Role> getRole() {
        return role;
    }

    public void setRole(List<Role> role) {
        if (role != null)
            this.role.addAll(role);
    }
}
