package com.esticharalegal.backendServer.repository;


import com.esticharalegal.backendServer.Enum.UserType;
import com.esticharalegal.backendServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByLicenseNumber(String licenseNumber);

    List<User> findByRole(UserType role);


    Optional<User> findByLicenseNumberAndEmail(String licenseNumber ,String email);
 }
