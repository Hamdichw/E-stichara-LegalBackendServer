package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/docs/Sign")
public class SignatureController {

    private final SignatureService signatureService;



    @PostMapping("/sign-document")
    public String signDocument(@RequestParam("documentId") long documentId, @RequestParam("userId") Long userId) {
        DocumentSigned documentSigned = signatureService.signDocument(documentId, userId);

        if (documentSigned != null) {
            return "Document signed successfully. Signature: " + documentSigned.getSignature();
        } else {
            return "Failed to sign the document. Please check the provided IDs.";
        }
    }

    @GetMapping("/verify")
    public String verifySignature(
            @RequestParam("documentSignedId") Long documentSignedId,
            @RequestParam("userId") Long userId
    ) {
        boolean isSignatureValid = signatureService.verifySignature(documentSignedId, userId);

        if (isSignatureValid) {
            return "Signature is valid for the given DocumentSigned ID and User ID.";
        } else {
            return "Signature is not valid for the given DocumentSigned ID and User ID.";
        }
    }
}