package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentSigned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentSignedRepository extends JpaRepository<DocumentSigned,Long> {

    Optional<DocumentSigned> findDocumentSignedByDocument(Document document);
}
