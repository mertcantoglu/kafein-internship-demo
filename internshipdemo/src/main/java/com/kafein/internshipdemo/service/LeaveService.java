package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.enums.LeaveStatus;
import com.kafein.internshipdemo.exceptions.DaysCantBeNegativeException;
import com.kafein.internshipdemo.exceptions.EmployeeNotEnoughDaysException;
import com.kafein.internshipdemo.exceptions.EmployeeNotFoundException;
import com.kafein.internshipdemo.exceptions.LeaveNotFoundException;
import com.kafein.internshipdemo.repository.LeaveRepository;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveService implements ILeaveService{


    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public List<Leave> findAll() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((user instanceof Employee)){
            return this.findByEmployeeId(user.getId());
        }
        return leaveRepository.findAll();
    }

    @Override
    public List<Leave> findByEmployeeId(int theId) {
        Employee employee = employeeService.findById(theId);
        return employee.getLeaves();
    }

    @Override
    public Leave findById(int theId) {
        return leaveRepository.findById(theId).orElse(null);
    }

    @Override
    @Transactional
    public Leave save(BreakUpdateRequestBody body ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(user instanceof Employee)){
            throw new RuntimeException("Only employees can create leaves");
        }

        Employee employee = employeeService.findById(user.getId());

        Leave leave = new Leave();
        leave.setEmployeeId(employee.getId());
        leave.setLeaveDay(body.getLeaveDay());
        leave.setReturnDay(body.getReturnDay());
        leave.setCreatedAt(System.currentTimeMillis());
        leave.setReason(body.getReason());
        leave.setLeaveHalfDay(body.isLeaveHalfDay());
        leave.setReturnHalfDay(body.isReturnHalfDay());
        leave.setStatus(LeaveStatus.PENDING);

        Double dayOff = leave.getDayDifference();
        Double newBreakDuration = employee.getNumDaysBreak() - dayOff;

        if (dayOff < 0){
            throw new DaysCantBeNegativeException("You can't take negative days off.");
        }
        if (newBreakDuration < 0){
            throw new EmployeeNotEnoughDaysException("You don't have enough leave rights for " + dayOff + " days.");
        }
        Leave dbLeave = employeeService.saveLeave(employee, leave);
        return dbLeave;
    }

    @Override
    @Transactional
    @Secured({"USER", "ADMIN"})
    public void deleteById(int theId) {
        Leave leave = leaveRepository.findById(theId).orElseThrow(() -> new LeaveNotFoundException("Leave not found"));
        Employee employee = employeeService.findById(leave.getEmployeeId());

        if (leave.getStatus() == LeaveStatus.APPROVE){
            Double dayOff = leave.getDayDifference();
            Double newBreakDuration = employee.getNumDaysBreak() + dayOff;
            employee.setNumDaysBreak(newBreakDuration);
        }

        employee.getLeaves().remove(leave);
        employeeService.update(employee);
        leaveRepository.deleteById(theId);
    }

    @Override
    @Transactional
    @Secured({"USER", "ADMIN"})
    public Leave updateLeaveRequestStatus(Integer id, LeaveStatus status) {

        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        if (status != LeaveStatus.APPROVE && status != LeaveStatus.REJECT) {
            throw new RuntimeException("Invalid status value: " + status);
        }

        // If the leave request is approved, the number of days off is updated.
        if (status == LeaveStatus.APPROVE) {
            Employee employee = employeeService.findById(leave.getEmployeeId());
            Double dayOff = leave.getDayDifference();
            Double newBreakDuration = employee.getNumDaysBreak() - dayOff;
            if (newBreakDuration < 0){
                throw new EmployeeNotEnoughDaysException("Employee doesn't have enough leave rights for " + dayOff + " days.");
            }
            employee.setNumDaysBreak(newBreakDuration);
            employeeService.update(employee);
        }


        leave.setStatus(status);
        leaveRepository.save(leave);
        return leave;
    }
}
