package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Appointment;
import com.esticharalegal.backendServer.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Appointments")
public class AppointmentController {

    private final AppointmentService appointmentService ;

    @PostMapping("/add/{idLAwyer}")
    public void addAppointment(
            @PathVariable Long idLAwyer ,
            @RequestParam("email") String email,
            @RequestParam("start") Date start,
            @RequestParam(name = "end", required = false) Date end
    ) throws AppException {
        Appointment appointment = new Appointment();
        if(end != null){
          appointment.setEnd(end);
        }
        appointment.setStart(start);
        this.appointmentService.addAppointment(idLAwyer , email ,appointment);

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
