package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.service.CloudService;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    private  final CloudService cloudService;
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
    public ResponseEntity<Map<String, String>> createDocument(
            @PathVariable Long userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ) {
        Map<String, String> response = new HashMap<>();
        Document document = new Document();
        document.setTitle(title);
        document.setDescription(description);
        document.setLinkFile(cloudService.uploadFile(file));
        try {
            document.setContent(file.getBytes());
        } catch (IOException e) {
            response.put("message", "Failed to process file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try {
            Document savedDocument = documentService.saveDocument(document, userId);

            if (savedDocument != null) {
                response.put("message", "Created successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("message", "User with ID " + userId + " does not exist");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("message", "Failed to create document");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
    @DeleteMapping("{id}/delete/{docId}")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable Long id , @PathVariable Long docId) {
        Map<String, String> response = new HashMap<>();

            boolean deleted = documentService.deleteDocument(id,docId);
            if (deleted) {
                response.put("message", "Deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("message", "Document not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
