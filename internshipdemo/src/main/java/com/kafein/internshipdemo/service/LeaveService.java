package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import com.kafein.internshipdemo.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveService implements ILeaveService{


    @Autowired
    private LeaveRepository leaveRepository;

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
    public Leave save(Leave theLeave) {
        return leaveRepository.save(theLeave);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        leaveRepository.deleteById(theId);
    }
}
