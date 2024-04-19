package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/docs/Sign")
public class SignatureController {

    private final SignatureService signatureService;



    @PostMapping("/sign-document")
    public ResponseEntity<Map<String, String>> signDocument(@RequestParam("documentId") long documentId, @RequestParam("userId") Long userId) throws AppException {
        DocumentSigned documentSigned = signatureService.signDocument(documentId, userId);
        Map<String, String> response = new HashMap<>();
        if (documentSigned != null) {
            response.put("message", "Document signed successfully. Signature: " );
        } else {
            response.put("message", "Failed to sign the document. Please check the provided IDs.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verifySignature(
            @RequestParam("documentSignedId") Long documentSignedId,
            @RequestParam("userId") Long userId
    ) throws AppException {
        boolean isSignatureValid = signatureService.verifySignature(documentSignedId, userId);
        Map<String, String> response = new HashMap<>();

        if (isSignatureValid) {
            response.put("message", "Signature is valid for the given DocumentSigned ID and User ID.");
        } else {
            response.put("message", "Signature is not valid for the given DocumentSigned ID and User ID.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}