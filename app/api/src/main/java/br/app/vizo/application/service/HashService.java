package br.app.vizo.application.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class HashService {

    public String hashToken(String token) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e) {
            return null;
        }
        var bytes = digest.digest(token.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
