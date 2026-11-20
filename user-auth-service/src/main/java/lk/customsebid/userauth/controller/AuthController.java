package lk.customsebid.userauth.controller;

import jakarta.validation.Valid;

import lk.customsebid.userauth.dto.RegisterUserRequest;
import lk.customsebid.userauth.dto.UserResponse;
import lk.customsebid.userauth.dto.AuthResponse;
import lk.customsebid.userauth.dto.LoginRequest;
import lk.customsebid.userauth.dto.ChangePasswordRequest;
import lk.customsebid.userauth.dto.PasswordChangeResponse;

import lk.customsebid.userauth.service.UserService;
import lk.customsebid.userauth.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(
            UserService userService,
            AuthService authService
    ) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        UserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<PasswordChangeResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        userService.changePassword(
                authentication.getName(),
                request
        );

        return ResponseEntity.ok(
                new PasswordChangeResponse(
                        "Password changed successfully. Please log in again."
                )
        );
    }
}