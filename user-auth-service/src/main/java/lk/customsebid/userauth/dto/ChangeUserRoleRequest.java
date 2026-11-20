package lk.customsebid.userauth.dto;

import jakarta.validation.constraints.NotNull;
import lk.customsebid.userauth.entity.UserRole;

public record ChangeUserRoleRequest(

        @NotNull(message = "User role is required")
        UserRole role

) {}