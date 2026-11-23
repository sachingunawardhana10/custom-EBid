
package lk.customsebid.userauth.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserSecurityTest {

    private User createUser() {
        return new User(
                "Test",
                "Bidder",
                "test@example.com",
                "hashed-password",
                "0711234567"
        );
    }

    @Test
    void changingStatusShouldIncrementCredentialVersion() {
        User user = createUser();

        user.changeStatus(UserStatus.SUSPENDED);

        assertEquals(1, user.getCredentialVersion());
        assertEquals(UserStatus.SUSPENDED, user.getStatus());
    }

    @Test
    void changingToSameStatusShouldNotIncrementCredentialVersion() {
        User user = createUser();

        user.changeStatus(UserStatus.ACTIVE);

        assertEquals(0, user.getCredentialVersion());
    }

    @Test
    void changingRoleShouldIncrementCredentialVersion() {
        User user = createUser();

        user.changeRole(UserRole.ADMIN);

        assertEquals(1, user.getCredentialVersion());
        assertEquals(UserRole.ADMIN, user.getRole());
    }

    @Test
    void changingToSameRoleShouldNotIncrementCredentialVersion() {
        User user = createUser();

        user.changeRole(UserRole.BIDDER);

        assertEquals(0, user.getCredentialVersion());
    }

    @Test
    void changingPasswordShouldIncrementCredentialVersion() {
        User user = createUser();

        user.changePassword("new-hashed-password");

        assertEquals(1, user.getCredentialVersion());
    }

    @Test
    void multipleSecurityChangesShouldIncrementVersionCorrectly() {
        User user = createUser();

        user.changePassword("new-hashed-password");
        user.changeRole(UserRole.ADMIN);
        user.changeStatus(UserStatus.SUSPENDED);

        assertEquals(3, user.getCredentialVersion());
    }
}
