package com.kafein.internshipdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    private String department;

    private Double numDaysBreak;

    public Employee() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Employee(String firstName, String lastName, String email, String department, Double numDaysBreak ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department =
                department;
        this.numDaysBreak=numDaysBreak;
    }

    public Double getNumDaysBreak() {
        return numDaysBreak;
    }

    public void setNumDaysBreak(Double numDaysBreak) {
        this.numDaysBreak = numDaysBreak;
    }
}
