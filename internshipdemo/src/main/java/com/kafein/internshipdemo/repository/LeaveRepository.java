package com.kafein.internshipdemo.repository;

import com.kafein.internshipdemo.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {
}
