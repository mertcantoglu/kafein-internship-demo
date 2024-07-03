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
import org.springframework.beans.factory.annotation.Autowired;
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
    public Leave saveLeave(@RequestBody BreakUpdateRequestBody body){
        Employee employee = employeeService.findById(body.getId());

        int days = (int)( (body.getReturnDay().getTime() - body.getLeaveDay().getTime()) / (1000 * 60 * 60 * 24) );

        if (employee == null){
            throw new EmployeeNotFoundException("Employee id not found: " + body.getId());
        }

        Integer newBreakDuration = employee.getNumDaysBreak() - days;

        if (days < 0){
            throw new DaysCantBeNegativeException("Employee can't take negative days off.");
        }
        if (newBreakDuration < 0){
            throw new EmployeeNotEnoughDaysException("Employee doesn't have enough leave rights for " + days + " days.");
        }

        employee.setNumDaysBreak(newBreakDuration);

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setLeaveDay(body.getLeaveDay());
        leave.setReturnDay(body.getReturnDay());
        leave.setCreatedAt(System.currentTimeMillis());
        leave.setReason(body.getReason());

        Leave dbLeave = leaveService.save(leave);
        employeeService.save(employee);
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
        employee.setNumDaysBreak(leave.getEmployee().getNumDaysBreak() + leave.getDayDifference() );
        employeeService.save(employee);
        return ( "Leave id deleted: "  + leaveId);
    }



}
