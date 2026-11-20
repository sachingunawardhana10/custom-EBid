package lk.customsebid.userauth.dto;

import jakarta.validation.constraints.NotNull;
import lk.customsebid.userauth.entity.UserStatus;

public record ChangeUserStatusRequest(

        @NotNull(message = "User status is required")
        UserStatus status

) {}