package com.esticharalegal.backendServer.service;

import com.esticharalegal.backendServer.model.Document;
import com.esticharalegal.backendServer.model.DocumentSigned;
import com.esticharalegal.backendServer.model.User;
import com.esticharalegal.backendServer.repository.DocumentRepository;
import com.esticharalegal.backendServer.repository.DocumentSignedRepository;
import com.esticharalegal.backendServer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class SignatureService{

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentSignedRepository documentSignedRepository;



    public DocumentSigned signDocument(long documentId, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if (userOptional.isEmpty() || documentOptional.isEmpty()) {
            // Handle user or document not found scenario
            return null;
        }

        User user = userOptional.get();
        Document document = documentOptional.get();

        try {
            byte[] documentContent = document.getContent();
            PrivateKey privateKey = getPrivateKeyFromBytes(user.getPrivateKey());
            String signature = signDocumentContent(documentContent, privateKey.getEncoded());

            DocumentSigned documentSigned = new DocumentSigned();
            documentSigned.setDocument(document);
            documentSigned.setSigner(user);
            documentSigned.setSignedDate(LocalDateTime.now());
            documentSigned.setSignature(signature);

            return documentSignedRepository.save(documentSigned);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean verifySignature(Long documentId, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Document> documentOptional = documentRepository.findById(documentId);

        if (userOptional.isEmpty() || documentOptional.isEmpty()) {
            return false;
        }

        User user = userOptional.get();
        Document document = documentOptional.get();

        try {
            // Fetch the latest signed document
            Optional<DocumentSigned> latestDocumentSignedOptional = documentSignedRepository.findById(document.getDocumentID());
            if (latestDocumentSignedOptional.isEmpty()) {
                return false;
            }

            DocumentSigned latestDocumentSigned = latestDocumentSignedOptional.get();
            byte[] documentContent = document.getContent();

            byte[] signatureBytes = Base64.getDecoder().decode(latestDocumentSigned.getSignature());
            PublicKey publicKey = getPublicKeyFromBytes(user.getPublicKey());

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(documentContent);

            return signature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return false;
    }


    private String signDocumentContent(byte[] documentContent, byte[] privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(getPrivateKeyFromBytes(privateKey));
            signature.update(documentContent);

            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
        return null;
    }

    private PrivateKey getPrivateKeyFromBytes(byte[] privateKeyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private PublicKey getPublicKeyFromBytes(byte[] publicKeyBytes) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}