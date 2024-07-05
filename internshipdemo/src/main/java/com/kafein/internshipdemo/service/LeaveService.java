package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.exceptions.DaysCantBeNegativeException;
import com.kafein.internshipdemo.exceptions.EmployeeNotEnoughDaysException;
import com.kafein.internshipdemo.exceptions.EmployeeNotFoundException;
import com.kafein.internshipdemo.exceptions.LeaveNotFoundException;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import com.kafein.internshipdemo.repository.LeaveRepository;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LeaveService implements ILeaveService{


    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public List<Leave> findAll() {
        return leaveRepository.findAll();
    }

    @Override
    public List<Leave> findByEmployeeId(int theId) {
        List<Leave> leaveList = leaveRepository
                .findAll()
                .stream()
                .filter(leave -> leave.getEmployee().getId() == theId)
                .toList();
        return leaveList;
    }

    @Override
    public Leave findById(int theId) {
        return leaveRepository.findById(theId).orElse(null);
    }

    @Override
    @Transactional
    public Leave save(BreakUpdateRequestBody body ) {

        Employee employee = employeeService.findById(body.getId());

        if (employee == null){
            throw new EmployeeNotFoundException("Employee id not found: " + body.getId());
        }

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setLeaveDay(body.getLeaveDay());
        leave.setReturnDay(body.getReturnDay());
        leave.setCreatedAt(System.currentTimeMillis());
        leave.setReason(body.getReason());
        leave.setLeaveHalfDay(body.isLeaveHalfDay());
        leave.setReturnHalfDay(body.isReturnHalfDay());


        Double dayOff = leave.getDayDifference();
        Double newBreakDuration = employee.getNumDaysBreak() - dayOff;

        if (dayOff < 0){
            throw new DaysCantBeNegativeException("Employee can't take negative days off.");
        }
        if (newBreakDuration < 0){
            throw new EmployeeNotEnoughDaysException("Employee doesn't have enough leave rights for " + dayOff + " days.");
        }

        employee.setNumDaysBreak(newBreakDuration);
        employeeService.update(employee);
        return leaveRepository.save(leave);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        Leave leave = this.findById(theId);
        if (leave == null){
            throw new LeaveNotFoundException("Leave id not found: " + theId);
        }

        Employee employee = leave.getEmployee();
        employee.setNumDaysBreak(leave.getEmployee().getNumDaysBreak() + leave.getDayDifference());
        employeeService.update(employee);

        leaveRepository.deleteById(theId);
    }
}
