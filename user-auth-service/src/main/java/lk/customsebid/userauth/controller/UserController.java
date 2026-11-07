package lk.customsebid.userauth.controller;

import jakarta.validation.Valid;

import lk.customsebid.userauth.dto.UserProfileResponse;
import lk.customsebid.userauth.dto.UserProfileUpdateRequest;
import lk.customsebid.userauth.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        UserProfileResponse response =
                userService.getMyProfile(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }


    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserProfileUpdateRequest request
    ) {

        UserProfileResponse response =
                userService.updateMyProfile(
                        userDetails.getUsername(),
                        request
                );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable UUID userId
    ) {

        UserProfileResponse response =
                userService.getUserProfile(userId);

        return ResponseEntity.ok(response);
    }
}