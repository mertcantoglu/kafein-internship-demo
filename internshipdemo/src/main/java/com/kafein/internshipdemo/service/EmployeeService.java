package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int theId) {
        Employee employee = null;
        Optional<Employee> result = employeeRepository.findById(theId);
        if (result.isPresent()){
            employee = result.get();
        }
       else {
            throw new RuntimeException("Employee can't find by id: " + theId);
        }
        return employee;
    }

    @Override
    @Transactional
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);

    }

}
