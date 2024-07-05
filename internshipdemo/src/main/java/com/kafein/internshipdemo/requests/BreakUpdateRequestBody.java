package com.kafein.internshipdemo.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class BreakUpdateRequestBody {


    @NotNull(message = "Id can't be empty")
    private Integer id;

    @NotNull(message = "Days can't be empty")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date leaveDay;

    @NotNull(message = "Days can't be empty")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date returnDay;

    @NotNull(message = "Reason can't be empty")
    private String reason;

    @NotNull(message = "Is leave half day can't be empty")
    private boolean leaveHalfDay;

    @NotNull(message = "Is return half day can't be empty")
    private boolean returnHalfDay;


}
