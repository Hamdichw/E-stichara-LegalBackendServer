package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentShared;
import com.esticharalegal.backendServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  DocumentSharedRepository  extends JpaRepository<DocumentShared, Long> {
    List<DocumentShared>  findBySharedWith(User user);
    List<DocumentShared>  findDocumentSharedByDocument(Document document);

    boolean deleteDocumentSharedByDocument_DocumentID(Long id);
}
