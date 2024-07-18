package com.kafein.internshipdemo.rest;

import com.kafein.internshipdemo.dto.EmployeeDTO;
import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")

public class EmployeeController {

    private EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<EmployeeDTO> getAllEmployees(){
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable int employeeId){
        return employeeService.getById(employeeId);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee){
        Employee dbEmployee = employeeService.update(employee);
        return dbEmployee;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId){
        employeeService.deleteById(employeeId);
        return ( "Employee id deleted: "  + employeeId);
    }

}
