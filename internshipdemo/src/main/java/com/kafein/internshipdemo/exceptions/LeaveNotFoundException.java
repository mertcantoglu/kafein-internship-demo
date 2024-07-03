package com.kafein.internshipdemo.exceptions;

public class LeaveNotFoundException extends RuntimeException{
    public LeaveNotFoundException(String message) {
        super(message);
    }
}
