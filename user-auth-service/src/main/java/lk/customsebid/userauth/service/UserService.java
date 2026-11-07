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
}