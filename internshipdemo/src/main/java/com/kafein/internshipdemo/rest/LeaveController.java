package com.kafein.internshipdemo.rest;

import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.enums.LeaveStatus;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;
import com.kafein.internshipdemo.service.EmployeeService;
import com.kafein.internshipdemo.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        leaveService.deleteById(leaveId);
        return ( "Leave id deleted: "  + leaveId);
    }

    @PutMapping("/leaves/{id}/status")
    public ResponseEntity<Leave> updateLeaveRequestStatus(@PathVariable Integer id, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        Leave updatedLeave = leaveService.updateLeaveRequestStatus(id, LeaveStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(updatedLeave);
    }




}
