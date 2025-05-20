package br.app.vizo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtService {

    @Value("${api.security.jwt.secret}")
    private String SECRET_KEY;

    private static final String ISSUER = "vizo";

    public String generateToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(this.createdAt())
                    .withExpiresAt(this.expiresAt())
                    .withSubject(user.getUsername())
                    .sign(algorithm);

        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error while generating token.", e);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid or expired token.");
        }
    }

    private Instant createdAt() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

    private Instant expiresAt() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(3).toInstant();
    }
}
