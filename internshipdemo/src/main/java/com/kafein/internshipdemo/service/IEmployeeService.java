package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findAll();
    Employee findById(int theId);
    Employee save(Employee theEmployee);

    Employee update(Employee theEmployee);
    void deleteById(int theId);
    Leave saveLeave(Employee theEmployee, Leave leave);


}
