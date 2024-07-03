package com.kafein.internshipdemo.rest;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;
import com.kafein.internshipdemo.service.EmployeeService;
import com.kafein.internshipdemo.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/leaves")
    public Leave saveLeave(@RequestBody BreakUpdateRequestBody body){
        Employee employee = employeeService.findById(body.getId());
        Integer newBreakDuration = employee.getNumDaysBreak() - body.getDays();

        if (newBreakDuration < 0 || body.getDays() < 0){
            throw new RuntimeException("Employee can't have negative break days.");
        }

        employee.setNumDaysBreak(newBreakDuration);

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setDays(body.getDays());
        leave.setCreatedAt(System.currentTimeMillis());

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
            throw new RuntimeException("Leave id not found: " + leaveId);
        }

        leaveService.deleteById(leaveId);
        Employee employee = leave.getEmployee();
        employee.setNumDaysBreak(leave.getEmployee().getNumDaysBreak() + leave.getDays());
        employeeService.save(employee);
        return ( "Leave id deleted: "  + leaveId);
    }



}
