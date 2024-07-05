package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.exceptions.DaysCantBeNegativeException;
import com.kafein.internshipdemo.exceptions.EmployeeNotFoundException;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    private EmployeeRepository employeeRepository;

   @Value("${employee.break.duration}")
   Double breakDuration;

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
            throw new EmployeeNotFoundException("Employee can't find by id: " + theId);
        }
        return employee;
    }

    @Override
    @Transactional
    public Employee save(Employee theEmployee) {
        theEmployee.setId(0);
        theEmployee.setNumDaysBreak(breakDuration);
        return employeeRepository.save(theEmployee);
    }

    @Override
    @Transactional
    public Employee update(Employee theEmployee) {
        if (theEmployee.getNumDaysBreak() <0){
            throw new DaysCantBeNegativeException("Days can't be negative");
        }
        return employeeRepository.save(theEmployee);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        Employee employee = this.findById(theId);
        if (employee == null){
            throw new EmployeeNotFoundException("Employee id not found: " + theId);
        }
        employeeRepository.deleteById(theId);

    }

}
