package com.kafein.internshipdemo.exceptions;

public class EmployeeNotEnoughDaysException extends RuntimeException{
    public EmployeeNotEnoughDaysException(String message) {
        super(message);
    }
}
