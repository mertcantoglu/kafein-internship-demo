package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findAll();
    Employee findById(int theId);
    Employee save(Employee theEmployee);

    Employee update(Employee theEmployee);
    void deleteById(int theId);


}
