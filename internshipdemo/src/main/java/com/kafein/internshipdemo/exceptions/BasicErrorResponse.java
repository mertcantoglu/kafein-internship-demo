package com.kafein.internshipdemo.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasicErrorResponse {
    private Integer status;
    private String message;
    private long timeStamp;
}
