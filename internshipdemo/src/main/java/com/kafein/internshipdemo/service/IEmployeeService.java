package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.dto.EmployeeDTO;
import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.enums.LeaveStatus;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeDTO> findAll();
    Employee findById(int theId);
    EmployeeDTO getById(int theId);
    Employee save(Employee theEmployee);

    Employee update(Employee theEmployee);
    void deleteById(int theId);
    Leave saveLeave(Employee theEmployee, Leave leave);
    List<EmployeeDTO> findByStatus(LeaveStatus status);


}
