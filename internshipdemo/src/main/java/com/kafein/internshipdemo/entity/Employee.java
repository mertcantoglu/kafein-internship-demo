package com.kafein.internshipdemo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "employee")
public class Employee extends User{

    private String department;
    private Double numDaysBreak;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Leave> leaves;


}
