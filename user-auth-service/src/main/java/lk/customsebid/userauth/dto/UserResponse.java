package lk.customsebid.userauth.dto;

import lk.customsebid.userauth.entity.UserRole;
import lk.customsebid.userauth.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(

        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        UserRole role,
        UserStatus status,
        Instant createdAt

) {
}