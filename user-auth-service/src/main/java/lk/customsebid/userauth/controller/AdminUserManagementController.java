package lk.customsebid.userauth.controller;

import jakarta.validation.Valid;

import lk.customsebid.userauth.dto.ChangeUserRoleRequest;
import lk.customsebid.userauth.dto.ChangeUserStatusRequest;
import lk.customsebid.userauth.dto.UserProfileResponse;
import lk.customsebid.userauth.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserManagementController {

    private final UserService userService;

    public AdminUserManagementController(UserService userService) {
        this.userService = userService;
    }

    // Retrieve all registered users.
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Retrieve a specific user.
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserById(
            @PathVariable UUID userId
    ) {

        return ResponseEntity.ok(
                userService.getUserById(userId)
        );
    }

    // Change account status.
    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserProfileResponse> updateUserStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUserStatus(
                        userId,
                        request.status()
                )
        );
    }

    // Change user role.
    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserProfileResponse> updateUserRole(
            @PathVariable UUID userId,
            @Valid @RequestBody ChangeUserRoleRequest request
    ) {

        return ResponseEntity.ok(
                userService.updateUserRole(
                        userId,
                        request.role()
                )
        );
    }
}