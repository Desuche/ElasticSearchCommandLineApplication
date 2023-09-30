package org.example;

import java.util.Date;
import java.util.UUID;

public class Person {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String fullName;
    private int age;
    private Date createdAt;

    public Person(){

    }

    public Person(String firstName, String lastName, int age) {
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.createdAt = new Date();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    public int getAge() {
        return age;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
