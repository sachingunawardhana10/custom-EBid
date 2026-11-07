package lk.customsebid.userauth.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}