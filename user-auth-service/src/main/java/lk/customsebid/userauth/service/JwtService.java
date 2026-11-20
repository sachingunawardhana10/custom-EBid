package lk.customsebid.userauth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lk.customsebid.userauth.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(UserDetails userDetails) {

        if (!(userDetails instanceof CustomUserDetails customUser)) {
            throw new IllegalArgumentException(
                    "CustomUserDetails is required to generate a token"
            );
        }

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim(
                        "credentialVersion",
                        customUser.getCredentialVersion()
                )
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        try {
            Claims claims = extractAllClaims(token);

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            Number tokenVersion =
                    claims.get("credentialVersion", Number.class);

            if (!(userDetails instanceof CustomUserDetails customUser)
                    || tokenVersion == null) {
                return false;
            }

            return username.equals(userDetails.getUsername())
                    && expiration.after(new Date())
                    && tokenVersion.longValue()
                    == customUser.getCredentialVersion()
                    && userDetails.isEnabled()
                    && userDetails.isAccountNonLocked();

        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    public long getExpirationSeconds() {
        return expirationMs / 1000;
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}