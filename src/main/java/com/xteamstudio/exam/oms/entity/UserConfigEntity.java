package com.xteamstudio.exam.oms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_CONFIG")
public class UserConfigEntity {

    @Id
    @Column(name = "UID")
    private String id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASS")
    private String pass;

    @Column(name = "ROLE")
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
