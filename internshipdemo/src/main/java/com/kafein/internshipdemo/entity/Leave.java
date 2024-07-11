package com.kafein.internshipdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "leaves")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Leave {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private Employee employee;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date leaveDay;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date returnDay;

    private String reason;

    private Long createdAt;

    private boolean leaveHalfDay;
    private boolean returnHalfDay;


    public Double getDayDifference(){
        Long milisecondsInDay = 1000 * 60 * 60 * 24L;
        Double dayOff = (double)((this.returnDay.getTime() - this.leaveDay.getTime()) / milisecondsInDay);

        if(this.isReturnHalfDay()) {
            dayOff -= 0.5;
        }
        if(this.isLeaveHalfDay()) dayOff -= 0.5;
        return (dayOff);
    }

}
