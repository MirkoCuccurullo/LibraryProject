package com.example.libraryproject.structure;

import java.io.Serializable;

public class Member implements Serializable {

    String dateOfBirth;
    String firstName;
    String surname;
    int id;

    public Member(int id, String firstName, String surname, String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;

    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
