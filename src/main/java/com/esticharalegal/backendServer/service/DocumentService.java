package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentShared;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.DocumentRepository;
import com.esticharalegal.backendServer.repository.DocumentSharedRepository;
import com.esticharalegal.backendServer.repository.DocumentSignedRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentSharedRepository documentSharedRepository;
    private  final DocumentSignedRepository documentSignedRepository;
    private final UserRepository userRepository;

    public Document saveDocument(Document document, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            document.setUploadedBy(user);
            document.setUploadedByUserName(user.getFirstName() + " " + user.getLastName());
            document.setUploadDate(LocalDateTime.now());
            return documentRepository.save(document);
        } else {
            return null;
        }
    }

    public List<Document> getAllDocuments(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);

        List<Document> myDocs = user.map(u -> documentRepository.findDocumentByUploadedByIs(u))
                .orElseGet(Collections::emptyList);

        List<DocumentShared> sharedWithMe = user.map(u -> documentSharedRepository.findBySharedWith(u))
                .orElseGet(Collections::emptyList);

        List<Document> allDocuments = myDocs.stream()
                                             .collect(Collectors.toList());
        allDocuments.addAll(sharedWithMe.stream()
                     .map(DocumentShared::getDocument)
                     .collect(Collectors.toList()));

        return allDocuments;
    }
    public Optional<Document> findDocumentById(Long id) {
        return documentRepository.findById(id);
    }
    public Optional<DocumentSigned> findDocumentSignedById(Long id) {
        return documentSignedRepository.findById(id);
    }

    public boolean deleteDocument(Long id, Long docId) {
        // Find the user
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return false; // No need to proceed if user doesn't exist

        // Find the document
        Optional<Document> document = documentRepository.findById(docId);
        if (document.isPresent()) {
            // Check if the user uploaded this document
            if (user.get().getUsername().equals(document.get().getUploadedByUserName())) {
                documentRepository.deleteById(docId);
                return true;
            } else {
                // Check if the document is shared with the user
                List<DocumentShared> documentShareds = documentSharedRepository.findDocumentSharedByDocument(document.get());
                for (DocumentShared documentShared : documentShareds) {
                    if (documentShared.getSharedWith().getUsername().equals(user.get().getUsername())) {
                        // If the document is shared with the user, delete it
                        documentSharedRepository.deleteDocumentSharedByDocument_DocumentID(docId);
                    }
                }
                return true;
            }
        }
        return false; // If none of the conditions match, return false
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
    public void  ShareDocument(String docId,String userId) throws AppException {
        long docIdLong;
        try {
            docIdLong = Long.parseLong(docId);
        } catch (NumberFormatException e) {
            throw new AppException("Invalid ID format", HttpStatus.BAD_REQUEST);
        }
        Optional<Document> existingDocumentOptional = documentRepository.findById(docIdLong);
        Optional<User> existingUserOptional = userRepository.findByEmail(userId);
        if(existingUserOptional.isEmpty() || existingDocumentOptional.isEmpty()){
            throw  new AppException("error ", HttpStatus.NOT_FOUND);
        }else{
            DocumentShared doc = new DocumentShared();
            doc.setDocument(existingDocumentOptional.get());
            doc.setSharedWith(existingUserOptional.get());
            documentSharedRepository.save(doc);
            throw  new AppException("shared success ", HttpStatus.OK);
        }

    }



}
