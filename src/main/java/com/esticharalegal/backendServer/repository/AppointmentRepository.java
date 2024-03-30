package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.Enum.AppointmentType;
import com.esticharalegal.backendServer.model.Appointment;
import com.esticharalegal.backendServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByClientAndLawyerAndStart(User client, User lawyer, Date start);


    List<Appointment> findByStatusAndClient(AppointmentType status,User client);

    Optional<List<Appointment>> findByLawyer(User lawyer);
}
