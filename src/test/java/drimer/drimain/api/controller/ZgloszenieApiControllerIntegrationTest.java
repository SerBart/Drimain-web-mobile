package drimer.drimain.api.controller;

import drimer.drimain.model.Dzial;
import drimer.drimain.model.Role;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.RoleRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ZgloszenieApiControllerIntegrationTest {

    @Autowired
    private ZgloszenieRepository zgloszenieRepository;

    @Autowired
    private DzialRepository dzialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldCreateZgloszenieWithDualSchemaSupport() {
        // Setup test data
        setupTestData();

        // Verify that we can create zgloszenia with the new schema
        User biuroUser = userRepository.findByUsername("biuro").orElseThrow();
        Dzial testDzial = biuroUser.getDzial();

        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setTytul("Test Zgloszenie");
        zgloszenie.setOpis("Test opis");
        zgloszenie.setStatus(ZgloszenieStatus.NEW);
        zgloszenie.setDzial(testDzial);
        zgloszenie.setAutor(biuroUser);

        Zgloszenie saved = zgloszenieRepository.save(zgloszenie);
        
        assertNotNull(saved.getId());
        assertEquals("Test Zgloszenie", saved.getTytul());
        assertEquals(ZgloszenieStatus.NEW, saved.getStatus());
        assertEquals(testDzial.getId(), saved.getDzial().getId());
        assertEquals(biuroUser.getId(), saved.getAutor().getId());
    }

    @Test
    public void shouldSupportBackwardCompatibilityWithOldSchema() {
        // Verify that old schema fields still work
        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setTyp("Old Type");
        zgloszenie.setImie("Jan");
        zgloszenie.setNazwisko("Kowalski");
        zgloszenie.setStatus(ZgloszenieStatus.OPEN);
        zgloszenie.setOpis("Old style opis with minimum length");

        Zgloszenie saved = zgloszenieRepository.save(zgloszenie);

        assertNotNull(saved.getId());
        assertEquals("Old Type", saved.getTyp());
        assertEquals("Jan", saved.getImie());
        assertEquals("Kowalski", saved.getNazwisko());
        assertEquals(ZgloszenieStatus.OPEN, saved.getStatus());
    }

    @Test
    public void shouldCreateUserWithDepartment() {
        setupTestData();

        User biuroUser = userRepository.findByUsername("biuro").orElseThrow();
        assertNotNull("User should have department assigned", biuroUser.getDzial());
        assertEquals("Test Dzial", biuroUser.getDzial().getNazwa());
        
        // Check that ROLE_BIURO exists
        Role biuroRole = roleRepository.findByName("ROLE_BIURO").orElseThrow();
        assertNotNull("ROLE_BIURO should be created", biuroRole);
        assertEquals("ROLE_BIURO", biuroRole.getName());
    }

    @Test
    public void shouldSupportBothOldAndNewStatusValues() {
        // Test that both old and new status enum values work
        Zgloszenie oldStyle = new Zgloszenie();
        oldStyle.setStatus(ZgloszenieStatus.OPEN);
        oldStyle.setOpis("Old status test");
        Zgloszenie savedOld = zgloszenieRepository.save(oldStyle);
        assertEquals(ZgloszenieStatus.OPEN, savedOld.getStatus());

        Zgloszenie newStyle = new Zgloszenie();
        newStyle.setStatus(ZgloszenieStatus.NEW);
        newStyle.setOpis("New status test");
        Zgloszenie savedNew = zgloszenieRepository.save(newStyle);
        assertEquals(ZgloszenieStatus.NEW, savedNew.getStatus());
    }

    @Test
    public void shouldValidateRepositoryMethods() {
        setupTestData();
        
        User testUser = userRepository.findByUsername("biuro").orElseThrow();
        Dzial testDzial = testUser.getDzial();
        
        // Create test zgloszenia
        Zgloszenie zgloszenie1 = new Zgloszenie();
        zgloszenie1.setTytul("Test 1");
        zgloszenie1.setOpis("Test opis 1");
        zgloszenie1.setStatus(ZgloszenieStatus.NEW);
        zgloszenie1.setDzial(testDzial);
        zgloszenie1.setAutor(testUser);
        zgloszenieRepository.save(zgloszenie1);

        Zgloszenie zgloszenie2 = new Zgloszenie();
        zgloszenie2.setTytul("Test 2");
        zgloszenie2.setOpis("Test opis 2");
        zgloszenie2.setStatus(ZgloszenieStatus.ACCEPTED);
        zgloszenie2.setDzial(testDzial);
        zgloszenie2.setAutor(testUser);
        zgloszenieRepository.save(zgloszenie2);

        // Test repository methods
        var byDzial = zgloszenieRepository.findByDzial_Id(
            testDzial.getId(), 
            org.springframework.data.domain.PageRequest.of(0, 10)
        );
        assertTrue("Should find zgloszenia by dzial", byDzial.getTotalElements() >= 2);

        var byStatus = zgloszenieRepository.findByStatus(
            ZgloszenieStatus.NEW,
            org.springframework.data.domain.PageRequest.of(0, 10)
        );
        assertTrue("Should find zgloszenia by status", byStatus.getTotalElements() >= 1);

        var byDzialAndStatus = zgloszenieRepository.findByDzial_IdAndStatus(
            testDzial.getId(),
            ZgloszenieStatus.ACCEPTED,
            org.springframework.data.domain.PageRequest.of(0, 10)
        );
        assertTrue("Should find zgloszenia by dzial and status", byDzialAndStatus.getTotalElements() >= 1);
    }

    private void setupTestData() {
        // Create roles if they don't exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));
        Role biuroRole = roleRepository.findByName("ROLE_BIURO")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_BIURO")));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        // Create test department
        Dzial dzial = new Dzial();
        dzial.setNazwa("Test Dzial");
        dzial = dzialRepository.save(dzial);

        // Create biuro user
        if (!userRepository.findByUsername("biuro").isPresent()) {
            User biuroUser = new User();
            biuroUser.setUsername("biuro");
            biuroUser.setPassword(passwordEncoder.encode("password"));
            biuroUser.setRoles(Set.of(biuroRole, userRole));
            biuroUser.setDzial(dzial);
            userRepository.save(biuroUser);
        }

        // Create regular user
        if (!userRepository.findByUsername("regular").isPresent()) {
            User regularUser = new User();
            regularUser.setUsername("regular");
            regularUser.setPassword(passwordEncoder.encode("password"));
            regularUser.setRoles(Set.of(userRole));
            regularUser.setDzial(dzial);
            userRepository.save(regularUser);
        }
    }
}