package br.app.vizo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
public class JwtService {

    @Value("${api.security.jwt.secret}")
    private String SECRET_KEY;

    private static final String ISSUER = "vizo";

    private String generateToken(String subject, String type, Instant expiresAt) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withIssuedAt(this.now())
                    .withExpiresAt(expiresAt)
                    .withSubject(subject)
                    .withClaim("type", type)
                    .sign(algorithm);

        } catch(JWTCreationException e) {
            throw new JWTCreationException("Error while generating token.", e);
        }
    }

    public String generateAccessToken(String subject) {
        return this.generateToken(subject, "access", this.accessTokenExpiresAt());
    }

    public String generateRefreshToken(String subject) {
        return this.generateToken(subject, "refresh", this.refreshTokenExpiresAt());
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

    public boolean isAccessTokenValid(String token) {
        Claim type = this.parseAllClaims(token).get("type");
        return type != null && type.asString().equals("access");
    }

    public boolean isRefreshTokenValid(String token) {
        Claim type = this.parseAllClaims(token).get("type");
        return type != null && type.asString().equals("refresh");
    }

    private Map<String, Claim> parseAllClaims(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getClaims();

        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Invalid or expired token.");
        }
    }

    private Instant now() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

    public Instant accessTokenExpiresAt() {
        return ZonedDateTime
                .now(ZoneId.of("America/Sao_Paulo"))
                .plusHours(3)
                .toInstant();
    }

    public Instant refreshTokenExpiresAt() {
        return ZonedDateTime
                .now(ZoneId.of("America/Sao_Paulo"))
                .plusDays(30)
                .toInstant();
    }
}
