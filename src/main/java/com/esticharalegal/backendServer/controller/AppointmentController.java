package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.Enum.AppointmentType;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Appointment;
import com.esticharalegal.backendServer.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Appointments")
public class AppointmentController {

    private final AppointmentService appointmentService ;

    @PostMapping("/add/{idLawyer}")
    public void addAppointment(
            @PathVariable Long idLawyer,
            @RequestParam("email") String email,
            @RequestParam("start") String start,
            @RequestParam(name = "end", required = false) String end
    ) throws AppException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date startDate;
        Date endDate = null;
        try {
            startDate = dateFormat.parse(start);
            if (end != null) {
                endDate = dateFormat.parse(end);
            }
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            throw new AppException("error" , HttpStatus.CONFLICT);
        }

        Appointment appointment = new Appointment();
        appointment.setStart(startDate);
        appointment.setEnd(endDate);
        appointment.setStatus(AppointmentType.Accepted);
        this.appointmentService.addAppointment(idLawyer, email, appointment);
    }
    @PostMapping("/request")
    public ResponseEntity<String> addRequestAppointment(@RequestBody Appointment appointment) {
        try {
            appointmentService.addRequestAppointment(appointment);
            return ResponseEntity.ok("Request added successfully");
        } catch (RuntimeException | AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/accept/{appointmentId}")
    public ResponseEntity<String> acceptAppointment(@PathVariable Long appointmentId) {
        try {
            appointmentService.acceptAppointment(appointmentId);
            return ResponseEntity.ok("Appointment accepted");
        } catch (RuntimeException | AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/accepted/{clientId}")
    public List<Appointment> getAllAcceptedAppointments(@PathVariable Long clientId) throws AppException {
        return appointmentService.getAllAcceptedAppointments(clientId);
    }

    @GetMapping("/notAccepted/{clientId}")
    public List<Appointment> getAllNotAcceptedAppointments(@PathVariable Long clientId) throws AppException {
        return appointmentService.getAllNotAcceptedAppointments(clientId);
    }

    @GetMapping("/byLawyer/{lawyerId}")
    public List<Appointment> getAllAppointmentsByLawyerId(@PathVariable Long lawyerId) throws AppException {
            return appointmentService.getAllAppointmentsByLawyerId(lawyerId);
    }
}
