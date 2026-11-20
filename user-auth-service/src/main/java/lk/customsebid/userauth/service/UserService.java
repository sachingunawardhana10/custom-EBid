package lk.customsebid.userauth.service;

import lk.customsebid.userauth.dto.RegisterUserRequest;
import lk.customsebid.userauth.dto.UserResponse;
import lk.customsebid.userauth.entity.User;
import lk.customsebid.userauth.entity.UserRole;
import lk.customsebid.userauth.entity.UserStatus;
import lk.customsebid.userauth.exception.EmailAlreadyRegisteredException;
import lk.customsebid.userauth.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import lk.customsebid.userauth.dto.UserProfileResponse;
import lk.customsebid.userauth.dto.UserProfileUpdateRequest;
import lk.customsebid.userauth.exception.UserNotFoundException;

import java.util.UUID;

import lk.customsebid.userauth.dto.ChangePasswordRequest;
import lk.customsebid.userauth.exception.PasswordChangeException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse registerUser(RegisterUserRequest request) {

        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyRegisteredException(
                    "An account with this email already exists"
            );
        }

        String hashedPassword = passwordEncoder.encode(
                request.password()
        );

        User user = new User(
                request.firstName().trim(),
                request.lastName().trim(),
                normalizedEmail,
                hashedPassword,
                request.phone() == null || request.phone().isBlank()
                        ? null
                        : request.phone().trim()
        );

        // Public registration must always create a BIDDER.
        user.changeStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                UserRole.BIDDER,
                savedUser.getStatus(),
                savedUser.getCreatedAt()
        );


    }
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(String email) {

        User user = findUserByEmail(email);

        return mapToProfileResponse(user);
    }


    @Transactional
    public UserProfileResponse updateMyProfile(
            String email,
            UserProfileUpdateRequest request
    ) {

        User user = findUserByEmail(email);

        String normalizedPhone = request.phone() == null
                || request.phone().isBlank()
                ? null
                : request.phone().trim();

        user.updateProfile(
                request.firstName().trim(),
                request.lastName().trim(),
                normalizedPhone
        );

        return mapToProfileResponse(user);
    }


    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        return mapToProfileResponse(user);
    }


    private User findUserByEmail(String email) {

        String normalizedEmail = email.trim()
                .toLowerCase(Locale.ROOT);

        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );
    }


    private UserProfileResponse mapToProfileResponse(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void changePassword(
            String email,
            ChangePasswordRequest request
    ) {
        User user = findUserByEmail(email);

        // Verify the existing password.
        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPasswordHash()
        )) {
            throw new PasswordChangeException(
                    "Current password is incorrect"
            );
        }

        // Confirm that both new-password fields match.
        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new PasswordChangeException(
                    "New password and confirmation do not match"
            );
        }

        // Prevent reuse of the current password.
        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPasswordHash()
        )) {
            throw new PasswordChangeException(
                    "New password must be different from the current password"
            );
        }

        // Hash the new password and increment credential version.
        String newPasswordHash =
                passwordEncoder.encode(request.newPassword());

        user.changePassword(newPasswordHash);
    }

    @Transactional(readOnly = true)
    public List<UserProfileResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToProfileResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public UserProfileResponse getUserById(UUID userId) {

        return getUserProfile(userId);
    }


    @Transactional
    public UserProfileResponse updateUserStatus(
            UUID userId,
            UserStatus status
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.changeStatus(status);

        return mapToProfileResponse(user);
    }


    @Transactional
    public UserProfileResponse updateUserRole(
            UUID userId,
            UserRole role
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        user.changeRole(role);

        return mapToProfileResponse(user);
    }
}