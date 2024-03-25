package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.DocumentRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public Document saveDocument(Document document, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            document.setUploadedBy(user);
            document.setUploadDate(LocalDateTime.now());
            return documentRepository.save(document);
        } else {
            return null;
        }
    }

    public List<Document> getAllDocuments(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        return user.map(documentRepository::findDocumentByUploadedByIs).orElse(null);
    }
    public Optional<Document> findDocumentById(Long id) {
        return documentRepository.findById(id);
    }
    public boolean deleteDocument(Long id) {
        Optional<Document> document = documentRepository.findById(id);
        if(document.isPresent()){
            documentRepository.deleteById(id);
            return  true;
        }else{
            return  false;
        }

    }

    public Document updateDocument(Long id, Document document) {
        Optional<Document> existingDocumentOptional = documentRepository.findById(id);

        if (existingDocumentOptional.isPresent()) {
            Document existingDocument = existingDocumentOptional.get();
            existingDocument.setTitle(document.getTitle());
            existingDocument.setDescription(document.getDescription());
            existingDocument.setContent(document.getContent());


            return documentRepository.save(existingDocument);
        } else {
            return null;
        }
    }

}
