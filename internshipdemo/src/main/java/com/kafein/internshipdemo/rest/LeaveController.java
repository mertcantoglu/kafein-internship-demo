package com.kafein.internshipdemo.rest;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.exceptions.DaysCantBeNegativeException;
import com.kafein.internshipdemo.exceptions.EmployeeNotEnoughDaysException;
import com.kafein.internshipdemo.exceptions.EmployeeNotFoundException;
import com.kafein.internshipdemo.exceptions.LeaveNotFoundException;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;
import com.kafein.internshipdemo.service.EmployeeService;
import com.kafein.internshipdemo.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/leaves")
    public Leave saveLeave(@Valid @RequestBody BreakUpdateRequestBody body){
        Leave dbLeave = leaveService.save(body);
        return dbLeave;
    }

    @GetMapping("/leaves")
    public List<Leave> getAllLeaves(){
        return leaveService.findAll();
    }

    @GetMapping("/leaves/{employeeId}")
    public List<Leave> getLeavesByEmployeeId(@PathVariable int employeeId){
        return leaveService.findByEmployeeId(employeeId);
    }

    @DeleteMapping("/leaves/{leaveId}")
    public String deleteLeave(@PathVariable int leaveId){
        Leave leave = leaveService.findById(leaveId);
        if (leave == null){
            throw new LeaveNotFoundException("Leave id not found: " + leaveId);
        }

        leaveService.deleteById(leaveId);
        Employee employee = leave.getEmployee();
        employee.setNumDaysBreak(leave.getEmployee().getNumDaysBreak() + leave.getDayDifference());
        employeeService.update(employee);
        return ( "Leave id deleted: "  + leaveId);
    }



}
