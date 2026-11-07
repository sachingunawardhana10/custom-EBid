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
}