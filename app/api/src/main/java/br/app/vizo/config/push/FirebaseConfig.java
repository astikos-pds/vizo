package br.app.vizo.config.push;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service.account}")
    private String firebaseServiceAccount;

    @PostConstruct
    public void init() throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(this.firebaseServiceAccount);

        if (decodedBytes == null) return;

        InputStream firebaseServiceAccount = new ByteArrayInputStream(decodedBytes);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseServiceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
