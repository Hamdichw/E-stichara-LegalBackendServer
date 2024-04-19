package com.esticharalegal.backendServer.controller;

import com.esticharalegal.backendServer.exceptions.AppException;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/docs/Sign")
public class SignatureController {

    private final SignatureService signatureService;



    @PostMapping("/sign-document")
    public void signDocument(@RequestParam("documentId") long documentId, @RequestParam("userId") Long userId) throws AppException {
        DocumentSigned documentSigned = signatureService.signDocument(documentId, userId);

        if (documentSigned != null) {
            throw  new AppException("Document signed successfully. Signature: " + documentSigned.getSignature(), HttpStatus.OK) ;
        } else {
            throw  new AppException( "Failed to sign the document. Please check the provided IDs.", HttpStatus.OK);
        }
    }

    @GetMapping("/verify")
    public void verifySignature(
            @RequestParam("documentSignedId") Long documentSignedId,
            @RequestParam("userId") Long userId
    ) throws AppException {
        boolean isSignatureValid = signatureService.verifySignature(documentSignedId, userId);

        if (isSignatureValid) {
            throw  new AppException( "Signature is valid for the given DocumentSigned ID and User ID.", HttpStatus.OK);
        } else {
            throw  new AppException( "Signature is not valid for the given DocumentSigned ID and User ID.", HttpStatus.OK);
        }
    }
}