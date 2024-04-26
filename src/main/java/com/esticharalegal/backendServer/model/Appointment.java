package com.esticharalegal.backendServer.model;

import com.esticharalegal.backendServer.Enum.AppointmentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentID;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private User client;

    @ManyToOne
    @JoinColumn(name = "LawyerID")
    private User lawyer;

    @Column(name = "StartAppointment")
    private Date start;

    @Column(name = "EndAppointment")
    private Date end;


    @Column(name = "time")
    private Date time = new Date(System.currentTimeMillis());

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private AppointmentType status = AppointmentType.Accepted;

}
