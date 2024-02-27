package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LawyerRepository extends JpaRepository<Lawyer,Long> {
    Optional<Lawyer> findByLicenseNumber(String licenceNumber);
}
