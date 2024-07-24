package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.enums.LeaveStatus;
import com.kafein.internshipdemo.requests.BreakUpdateRequestBody;

import java.util.Date;
import java.util.List;

public interface ILeaveService {

    List<Leave> findAll();

    Leave findById(int theId);
    List<Leave> findByEmployeeId(int theId);
    Leave save(BreakUpdateRequestBody body);
    void deleteById(int theId);
    Leave updateLeaveRequestStatus(Integer id, LeaveStatus status);
}
