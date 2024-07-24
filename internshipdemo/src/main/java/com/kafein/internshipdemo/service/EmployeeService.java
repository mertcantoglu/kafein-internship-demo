package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.dto.EmployeeDTO;
import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.enums.LeaveStatus;
import com.kafein.internshipdemo.exceptions.DaysCantBeNegativeException;
import com.kafein.internshipdemo.exceptions.EmployeeNotFoundException;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<EmployeeDTO> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((user instanceof Employee)){
            return List.of(this.getById(user.getId()));
        }
        return employeeRepository.findAll()
                .stream().map(employee -> EmployeeDTO.builder()
                        .firstName(employee.getFirstName())
                        .id(employee.getId())
                        .lastName(employee.getLastName())
                        .email(employee.getEmail())
                        .leaves(employee.getLeaves())
                        .department(employee.getDepartment())
                        .numDaysBreak(employee.getNumDaysBreak())
                        .build())
                .collect(Collectors.toList());
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
    public EmployeeDTO getById(int theId) {

        Employee employee = null;
        Optional<Employee> result = employeeRepository.findById(theId);
        if (result.isPresent()){
            employee = result.get();
        }
        else {
            throw new EmployeeNotFoundException("Employee can't find by id: " + theId);
        }

        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .id(employee.getId())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .leaves(employee.getLeaves())
                .department(employee.getDepartment())
                .numDaysBreak(employee.getNumDaysBreak())
                .build();
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

    @Override
    public Leave saveLeave(Employee theEmployee , Leave theLeave) {
        theEmployee.getLeaves().add(theLeave);
        employeeRepository.save(theEmployee);

        return theLeave;
    }

    @Secured({"USER", "ADMIN"})
    public List<EmployeeDTO> findByStatus(LeaveStatus status) {
            return employeeRepository.findAll()
                    .stream()
                    .map(employee -> {
                        List<Leave> pendingLeaves = employee.getLeaves().stream()
                                .filter(leave -> leave.getStatus() == status)
                                .collect(Collectors.toList());

                        if (!pendingLeaves.isEmpty()) {
                            return EmployeeDTO.builder()
                                    .firstName(employee.getFirstName())
                                    .id(employee.getId())
                                    .lastName(employee.getLastName())
                                    .email(employee.getEmail())
                                    .leaves(pendingLeaves)
                                    .department(employee.getDepartment())
                                    .numDaysBreak(employee.getNumDaysBreak())
                                    .build();
                        }
                        return null;
                    })
                    .filter(employeeDTO -> employeeDTO != null)
                    .collect(Collectors.toList());
        }

    @Override
    public List<EmployeeDTO> findOffsByDate(Date date) {
        return employeeRepository.findAll()
                .stream()
                .map(employee -> {
                    List<Leave> dateLeaves = employee.getLeaves().stream()
                            .filter(leave -> leave.getStatus() == LeaveStatus.APPROVE)
                            .filter(leave -> leave.getReturnDay().after(date))
                            .filter(leave -> leave.getLeaveDay().before(date))
                            .collect(Collectors.toList());

                    if (!dateLeaves.isEmpty()) {
                        return EmployeeDTO.builder()
                                .firstName(employee.getFirstName())
                                .id(employee.getId())
                                .lastName(employee.getLastName())
                                .email(employee.getEmail())
                                .leaves(dateLeaves)
                                .department(employee.getDepartment())
                                .numDaysBreak(employee.getNumDaysBreak())
                                .build();
                    }
                    return null;
                })
                .filter(employeeDTO -> employeeDTO != null)
                .collect(Collectors.toList());
    }
}

