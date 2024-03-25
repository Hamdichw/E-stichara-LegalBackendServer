package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.DocumentSigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentSignedRepository extends JpaRepository<DocumentSigned,Long> {
}
