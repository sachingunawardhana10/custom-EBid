package lk.customsebid.userauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank
        String currentPassword,

        @NotBlank
        @Size(max = 72)
        String newPassword,

        @NotBlank
        @Size(max = 72)
        String confirmNewPassword

) {
}