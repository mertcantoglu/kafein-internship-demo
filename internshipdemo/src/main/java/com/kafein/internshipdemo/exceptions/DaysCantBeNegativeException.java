package com.kafein.internshipdemo.exceptions;

public class DaysCantBeNegativeException extends RuntimeException{
    public DaysCantBeNegativeException(String message) {
        super(message);
    }
}
