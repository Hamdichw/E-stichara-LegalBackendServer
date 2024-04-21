package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentSignedRepository extends JpaRepository<DocumentSigned,Long> {

    List<DocumentSigned> findDocumentSignedByDocument(Document document);


    List<DocumentSigned> findDocumentSignedBySignID(User user);
}
