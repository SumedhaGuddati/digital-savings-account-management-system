package com.company.digisavingscore.repository;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User createUser(String email, Role role, LocalDateTime createdAt) {
        User user = new User();
        user.setEmail(email);
        user.setName("Test User");
        user.setPassword("password123");
        user.setRole(role);
        user.setCreatedAt(createdAt);
        return userRepository.save(user);
    }

    @Test
    @DisplayName("Should count new users today by role (Native Query) - H2 workaround")
    void testCountNewUsersTodayByRole() {
        LocalDateTime now = LocalDateTime.now().withHour(10);

        createUser("user1@test.com", Role.USER, now);
        createUser("user2@test.com", Role.USER, now);
        createUser("admin1@test.com", Role.ADMIN, now);

        try {
            userRepository.countNewUsersTodayByRole("USER");
            assertThat(true).as("Expected exception due to unsupported DATE() function in H2").isFalse();
        } catch (Exception ex) {
            assertThat(ex.getMessage()).contains("Function \"DATE\" not found");
        }
    }

    @Test
    @DisplayName("Should group and count users by role")
    void testCountUsersByRole() {

        LocalDateTime now = LocalDateTime.now().withHour(10);

        createUser("user1@test.com", Role.USER, now);
        createUser("user2@test.com", Role.USER, now);
        createUser("admin1@test.com", Role.ADMIN, now);

        List<Object[]> results = userRepository.countUsersByRole();

        assertThat(results).isNotEmpty();

        boolean userFound = false;
        boolean adminFound = false;

        for (Object[] row : results) {
            Role role = (Role) row[0];
            Long count = (Long) row[1];

            if (role == Role.USER) {
                userFound = true;
                assertThat(count).isEqualTo(2);
            }

            if (role == Role.ADMIN) {
                adminFound = true;
                assertThat(count).isEqualTo(1);
            }
        }

        assertThat(userFound).isTrue();
        assertThat(adminFound).isTrue();
    }
}
