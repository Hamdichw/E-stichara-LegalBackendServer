package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;
    @GetMapping("/all/{idUser}")
    public ResponseEntity<List<Document>> getAllDocuments(@PathVariable Long idUser) {
        try {
            List<Document> documents = documentService.getAllDocuments(idUser);
            return new ResponseEntity<>(documents, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> createDocument(
            @PathVariable Long userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) {
        Document document = new Document();
        document.setTitle(title);
        document.setDescription(description);

        try {
            document.setContent(file.getBytes());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to process file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Document savedDocument = documentService.saveDocument(document, userId);

            if (savedDocument != null) {
                return new ResponseEntity<>("Created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("User with ID " + userId + " does not exist", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create document", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        try {
            Optional<Document> document = documentService.findDocumentById(id);
            return document.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        try {
            boolean deleted = documentService.deleteDocument(id);
            if(deleted){
                return new ResponseEntity<>("deleted" ,HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Not Found ",HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocument(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) {
        Document document = new Document();
        document.setDocumentID(id);
        document.setTitle(title);
        document.setDescription(description);

        try {
            document.setContent(file.getBytes());
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to process file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            Document updatedDocument = documentService.updateDocument(id, document);

            if (updatedDocument != null) {
                return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Document with ID " + id + " does not exist", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update document", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<InputStreamResource> downloadDocument(@PathVariable Long id) {
        try {
            Optional<Document> document = documentService.findDocumentById(id);
            if (document.isPresent()) {
                Document foundDocument = document.get();

                // Set the response content type as application/pdf
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "document_" + id + ".pdf");

                // Create InputStreamResource from the document content
                InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(foundDocument.getContent()));

                // Return ResponseEntity with file content and headers
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shareMyDocument")
    public void ShareMyDocument(@RequestParam("docId") String docId, @RequestParam("Email") String userId) throws AppException {
        documentService.ShareDocument(docId,userId);
    }
}
