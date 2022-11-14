package com.example.libraryproject.structure;

import java.io.Serializable;

public class User implements Serializable {

    private final String password;
    private final String username;
    private String name;
    private String lastName;
    private int id;


    public User(int id, String name, String lastName, String password, String username) {
        this.password = password;
        this.username = username;
        this.id = id;
        this.lastName = lastName;
        this.name = name;

    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }

}
