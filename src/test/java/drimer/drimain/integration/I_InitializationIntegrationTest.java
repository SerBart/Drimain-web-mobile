package drimer.drimain.integration;

import drimer.drimain.config.DataInitializer;
import drimer.drimain.config.RoleInitializer;
import drimer.drimain.model.Role;
import drimer.drimain.model.User;
import drimer.drimain.repository.RoleRepository;
import drimer.drimain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * I-INIT Checkpoint: Integration tests for application initialization
 * 
 * Tests verify that the application initializes correctly with:
 * - All required roles are created
 * - Default users are properly initialized
 * - Spring context loads successfully
 * - Database schema is created and populated
 */
@SpringBootTest
@DisplayName("I-INIT: Application Initialization Integration Tests")
public class I_InitializationIntegrationTest {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleInitializer roleInitializer;
    
    @Autowired
    private DataInitializer dataInitializer;

    @Test
    @DisplayName("I-INIT-001: Should initialize all required roles")
    void shouldInitializeAllRequiredRoles() {
        // Given: Application startup has completed
        
        // When: Fetching all roles from database
        List<Role> roles = roleRepository.findAll();
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        
        // Then: All required roles should exist
        assertThat(roleNames)
                .as("All required roles should be initialized")
                .containsExactlyInAnyOrder(
                    "ROLE_ADMIN", 
                    "ROLE_USER", 
                    "ROLE_MAGAZYN", 
                    "ROLE_BIURO",
                    "ADMIN",
                    "USER"
                );
    }

    @Test
    @DisplayName("I-INIT-002: Should create default admin user")
    void shouldCreateDefaultAdminUser() {
        // Given: Application startup has completed
        
        // When: Fetching admin user
        User adminUser = userRepository.findByUsername("admin").orElse(null);
        
        // Then: Admin user should exist with correct roles
        assertThat(adminUser)
                .as("Default admin user should be created")
                .isNotNull();
        
        assertThat(adminUser.getUsername())
                .as("Admin username should be correct")
                .isEqualTo("admin");
        
        Set<String> adminRoles = adminUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        
        assertThat(adminRoles)
                .as("Admin should have both ADMIN and USER roles")
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    @DisplayName("I-INIT-003: Should create default regular user")
    void shouldCreateDefaultRegularUser() {
        // Given: Application startup has completed
        
        // When: Fetching regular user
        User regularUser = userRepository.findByUsername("user").orElse(null);
        
        // Then: Regular user should exist with correct role
        assertThat(regularUser)
                .as("Default regular user should be created")
                .isNotNull();
        
        assertThat(regularUser.getUsername())
                .as("Regular user username should be correct")
                .isEqualTo("user");
        
        Set<String> userRoles = regularUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        
        assertThat(userRoles)
                .as("Regular user should have USER role only")
                .containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("I-INIT-004: Should have unique role names")
    void shouldHaveUniqueRoleNames() {
        // Given: Application startup has completed
        
        // When: Fetching all roles
        List<Role> roles = roleRepository.findAll();
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        
        // Then: Role names should be unique
        assertThat(roleNames)
                .as("Role names should be unique")
                .doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("I-INIT-005: Should have users with encrypted passwords")
    void shouldHaveUsersWithEncryptedPasswords() {
        // Given: Application startup has completed
        
        // When: Fetching users
        List<User> users = userRepository.findAll();
        
        // Then: All users should have encrypted passwords (not plain text)
        for (User user : users) {
            assertThat(user.getPassword())
                    .as("User %s should have encrypted password", user.getUsername())
                    .isNotNull()
                    .isNotEmpty()
                    .startsWith("$"); // BCrypt passwords start with $
        }
    }
}