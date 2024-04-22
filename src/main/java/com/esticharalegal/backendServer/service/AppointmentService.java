package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.Enum.AppointmentType;
import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Appointment;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.AppointmentRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private  final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public void addAppointment(Long idLawyer , String email ,Appointment appointment) throws AppException {
        Optional<User> client = userRepository.findByEmail(email);
        Optional<User> lawyer = userRepository.findById(idLawyer);

        // Check if client and lawyer exist
        if (client.isEmpty() || lawyer.isEmpty()) {
            throw new AppException("Client or Lawyer not found", HttpStatus.NOT_FOUND);
        }
        appointment.setClient(client.get());
        appointment.setLawyer(lawyer.get());
        if (appointmentRepository.existsByClientAndLawyerAndStart(appointment.getClient(), appointment.getLawyer(), appointment.getStart())) {
            throw new AppException("Appointment already exists for client, lawyer, and start time",HttpStatus.BAD_REQUEST);
        }
        appointmentRepository.save(appointment);
    }


    public void addRequestAppointment(Appointment appointment) throws AppException {
        if (appointmentRepository.existsByClientAndLawyerAndStart(appointment.getClient(), appointment.getLawyer(), appointment.getStart())) {
            throw new AppException("Appointment already exists for client, lawyer, and start time",HttpStatus.BAD_REQUEST);
        }else{
            appointment.setStatus(AppointmentType.NotAccepted);
            appointmentRepository.save(appointment);
        }

    }

    public void acceptAppointment(Long appointmentId) throws AppException{
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if(appointment.isPresent()){
           Appointment appointmentUpdate = appointment.get();
           appointmentUpdate.setStatus(AppointmentType.Accepted);
           appointmentRepository.save(appointmentUpdate);
        }else{
            throw new AppException("appointment doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    public List<Appointment> getAllAcceptedAppointments(Long ClientId) throws AppException {
        Optional<User> user = userRepository.findById(ClientId);
        if (user.isPresent()) {
            return appointmentRepository.findByStatusAndClient(AppointmentType.Accepted, user.get());
        } else {
            throw new AppException("User not found with ID: " + ClientId, HttpStatus.NOT_FOUND);
        }
    }

    public List<Appointment> getAllNotAcceptedAppointments(Long ClientId) throws AppException {
        Optional<User> user = userRepository.findById(ClientId);
        if (user.isPresent()) {
            return appointmentRepository.findByStatusAndClient(AppointmentType.NotAccepted, user.get());
        } else {
            throw new AppException("Appointments not found for user with ID: " + ClientId, HttpStatus.NOT_FOUND);
        }
    }
    public List<Appointment> getAllAppointmentsByLawyerId(Long LawyerId) throws AppException{
        Optional<User> Lawyer = userRepository.findById(LawyerId);
        if(Lawyer.isPresent()){
            Optional<List<Appointment>> appointments =  appointmentRepository.findByLawyer(Lawyer.get());
            return appointments.orElse(null);
        }else{
            throw new AppException("Appointments not found for user with ID: " + LawyerId, HttpStatus.NOT_FOUND);
        }

    }
}
