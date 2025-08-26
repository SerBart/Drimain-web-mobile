package drimer.drimain.service;

import drimer.drimain.api.dto.ZgloszenieCreateRequest;
import drimer.drimain.model.Dzial;
import drimer.drimain.model.Role;
import drimer.drimain.model.User;
import drimer.drimain.model.Zgloszenie;
import drimer.drimain.model.enums.ZgloszenieStatus;
import drimer.drimain.repository.DzialRepository;
import drimer.drimain.repository.RoleRepository;
import drimer.drimain.repository.UserRepository;
import drimer.drimain.repository.ZgloszenieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Transactional
public class ZgloszenieAccessTest {

    @Autowired
    private ZgloszenieService zgloszenieService;

    @Autowired
    private DzialRepository dzialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ZgloszenieRepository zgloszenieRepository;

    private Dzial konfekcja;
    private Dzial drukarnia;
    private Dzial biuro;

    private User u1; // ROLE_USER konfekcja
    private User u2; // ROLE_USER drukarnia
    private User biuroUser; // ROLE_BIURO biuro
    private User admin; // ROLE_ADMIN

    private Role roleUser;
    private Role roleBiuro;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        // Clean up existing test data
        zgloszenieRepository.deleteAll();
        
        // Create departments
        konfekcja = new Dzial();
        konfekcja.setNazwa("konfekcja");
        konfekcja = dzialRepository.save(konfekcja);

        drukarnia = new Dzial();
        drukarnia.setNazwa("drukarnia");
        drukarnia = dzialRepository.save(drukarnia);

        biuro = new Dzial();
        biuro.setNazwa("biuro");
        biuro = dzialRepository.save(biuro);

        // Create roles
        roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role role = new Role("ROLE_USER");
            return roleRepository.save(role);
        });

        roleBiuro = roleRepository.findByName("ROLE_BIURO").orElseGet(() -> {
            Role role = new Role("ROLE_BIURO");
            return roleRepository.save(role);
        });

        roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role("ROLE_ADMIN");
            return roleRepository.save(role);
        });

        // Create users (use unique usernames to avoid conflicts)
        u1 = userRepository.findByUsername("test_user1").orElseGet(() -> {
            User user = new User();
            user.setUsername("test_user1");
            user.setPassword("password");
            user.setDzial(konfekcja);
            user.addRole(roleUser);
            return userRepository.save(user);
        });

        u2 = userRepository.findByUsername("test_user2").orElseGet(() -> {
            User user = new User();
            user.setUsername("test_user2");
            user.setPassword("password");
            user.setDzial(drukarnia);
            user.addRole(roleUser);
            return userRepository.save(user);
        });

        biuroUser = userRepository.findByUsername("test_biuro").orElseGet(() -> {
            User user = new User();
            user.setUsername("test_biuro");
            user.setPassword("password");
            user.setDzial(biuro);
            user.addRole(roleBiuro);
            return userRepository.save(user);
        });

        admin = userRepository.findByUsername("test_admin").orElseGet(() -> {
            User user = new User();
            user.setUsername("test_admin");
            user.setPassword("password");
            user.setDzial(null); // Admin might not have a specific department
            user.addRole(roleAdmin);
            return userRepository.save(user);
        });
    }

    @Test
    void testRegularUserSeesOnlyOwnDepartmentZgloszenia() {
        // Given: Create zgłoszenia in different departments
        Zgloszenie zgloszenieKonfekcja = createZgloszenie("Zgłoszenie konfekcja", konfekcja, u1);
        Zgloszenie zgloszenieDrukarnia = createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        Pageable pageable = PageRequest.of(0, 10);

        // When: u1 lists zgłoszenia
        Page<Zgloszenie> u1Results = zgloszenieService.listForUser(u1, null, pageable, false, false);

        // Then: u1 sees only konfekcja zgłoszenia
        assertThat(u1Results.getContent()).hasSize(1);
        assertThat(u1Results.getContent().get(0).getTytul()).isEqualTo("Zgłoszenie konfekcja");
        assertThat(u1Results.getContent().get(0).getDzial().getNazwa()).isEqualTo("konfekcja");

        // When: u2 lists zgłoszenia
        Page<Zgloszenie> u2Results = zgloszenieService.listForUser(u2, null, pageable, false, false);

        // Then: u2 sees only drukarnia zgłoszenia
        assertThat(u2Results.getContent()).hasSize(1);
        assertThat(u2Results.getContent().get(0).getTytul()).isEqualTo("Zgłoszenie drukarnia");
        assertThat(u2Results.getContent().get(0).getDzial().getNazwa()).isEqualTo("drukarnia");
    }

    @Test
    void testBiuroUserSeesAllZgloszenia() {
        // Given: Create zgłoszenia in different departments
        createZgloszenie("Zgłoszenie konfekcja", konfekcja, u1);
        createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        Pageable pageable = PageRequest.of(0, 10);

        // When: biuro user lists zgłoszenia
        Page<Zgloszenie> biuroResults = zgloszenieService.listForUser(biuroUser, null, pageable, false, true);

        // Then: biuro sees all zgłoszenia
        assertThat(biuroResults.getContent()).hasSize(2);
    }

    @Test
    void testAdminSeesAllZgloszenia() {
        // Given: Create zgłoszenia in different departments
        createZgloszenie("Zgłoszenie konfekcja", konfekcja, u1);
        createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        Pageable pageable = PageRequest.of(0, 10);

        // When: admin lists zgłoszenia
        Page<Zgloszenie> adminResults = zgloszenieService.listForUser(admin, null, pageable, true, false);

        // Then: admin sees all zgłoszenia
        assertThat(adminResults.getContent()).hasSize(2);
    }

    @Test
    void testRegularUserCannotAccessOtherDepartmentZgloszenie() {
        // Given: Create zgłoszenie in drukarnia department
        Zgloszenie zgloszenieDrukarnia = createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        // When/Then: u1 (konfekcja) cannot access drukarnia zgłoszenie
        assertThatThrownBy(() -> 
            zgloszenieService.get(zgloszenieDrukarnia.getId(), u1, false, false))
            .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void testRegularUserCannotUpdateOtherDepartmentZgloszenie() {
        // Given: Create zgłoszenie in drukarnia department
        Zgloszenie zgloszenieDrukarnia = createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        // When/Then: u1 (konfekcja) cannot update drukarnia zgłoszenie
        assertThatThrownBy(() -> {
            var updateRequest = new drimer.drimain.api.dto.ZgloszenieUpdateRequest();
            updateRequest.setTytul("Updated title");
            zgloszenieService.update(zgloszenieDrukarnia.getId(), updateRequest, u1, false, false);
        }).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void testRegularUserCannotDeleteOtherDepartmentZgloszenie() {
        // Given: Create zgłoszenie in drukarnia department
        Zgloszenie zgloszenieDrukarnia = createZgloszenie("Zgłoszenie drukarnia", drukarnia, u2);

        // When/Then: u1 (konfekcja) cannot delete drukarnia zgłoszenie
        assertThatThrownBy(() -> 
            zgloszenieService.delete(zgloszenieDrukarnia.getId(), u1, false, false))
            .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    void testBiuroCanAccessAllDepartmentZgloszenia() {
        // Given: Create zgłoszenie in konfekcja department
        Zgloszenie zgloszenieKonfekcja = createZgloszenie("Zgłoszenie konfekcja", konfekcja, u1);

        // When: biuro user accesses konfekcja zgłoszenie
        Zgloszenie result = zgloszenieService.get(zgloszenieKonfekcja.getId(), biuroUser, false, true);

        // Then: access is granted
        assertThat(result).isNotNull();
        assertThat(result.getTytul()).isEqualTo("Zgłoszenie konfekcja");
    }

    @Test
    void testAdminCanAccessAllDepartmentZgloszenia() {
        // Given: Create zgłoszenie in konfekcja department
        Zgloszenie zgloszenieKonfekcja = createZgloszenie("Zgłoszenie konfekcja", konfekcja, u1);

        // When: admin accesses konfekcja zgłoszenie
        Zgloszenie result = zgloszenieService.get(zgloszenieKonfekcja.getId(), admin, true, false);

        // Then: access is granted
        assertThat(result).isNotNull();
        assertThat(result.getTytul()).isEqualTo("Zgłoszenie konfekcja");
    }

    @Test
    void testRegularUserForcedToOwnDepartmentOnCreate() {
        // Given: u1 from konfekcja tries to create zgłoszenie
        ZgloszenieCreateRequest request = new ZgloszenieCreateRequest();
        request.setTytul("Test zgłoszenie");
        request.setOpis("Test opis dla zgłoszenia");
        request.setDzialId(drukarnia.getId()); // Try to set different department

        // When: u1 creates zgłoszenie
        Zgloszenie created = zgloszenieService.create(request, u1, false, false);

        // Then: zgłoszenie is created in u1's department (konfekcja), not drukarnia
        assertThat(created.getDzial().getId()).isEqualTo(konfekcja.getId());
        assertThat(created.getDzial().getNazwa()).isEqualTo("konfekcja");
        assertThat(created.getAutor().getId()).isEqualTo(u1.getId());
    }

    @Test
    void testBiuroCanSpecifyDepartmentOnCreate() {
        // Given: biuro user creates zgłoszenie with specific department
        ZgloszenieCreateRequest request = new ZgloszenieCreateRequest();
        request.setTytul("Test zgłoszenie");
        request.setOpis("Test opis dla zgłoszenia");
        request.setDzialId(konfekcja.getId());

        // When: biuro user creates zgłoszenie
        Zgloszenie created = zgloszenieService.create(request, biuroUser, false, true);

        // Then: zgłoszenie is created in specified department
        assertThat(created.getDzial().getId()).isEqualTo(konfekcja.getId());
        assertThat(created.getDzial().getNazwa()).isEqualTo("konfekcja");
        assertThat(created.getAutor().getId()).isEqualTo(biuroUser.getId());
    }

    @Test
    void testAdminCanSpecifyDepartmentOnCreate() {
        // Given: admin creates zgłoszenie with specific department
        ZgloszenieCreateRequest request = new ZgloszenieCreateRequest();
        request.setTytul("Test zgłoszenie");
        request.setOpis("Test opis dla zgłoszenia");
        request.setDzialId(drukarnia.getId());

        // When: admin creates zgłoszenie
        Zgloszenie created = zgloszenieService.create(request, admin, true, false);

        // Then: zgłoszenie is created in specified department
        assertThat(created.getDzial().getId()).isEqualTo(drukarnia.getId());
        assertThat(created.getDzial().getNazwa()).isEqualTo("drukarnia");
        assertThat(created.getAutor().getId()).isEqualTo(admin.getId());
    }

    private Zgloszenie createZgloszenie(String tytul, Dzial dzial, User autor) {
        Zgloszenie zgloszenie = new Zgloszenie();
        zgloszenie.setTytul(tytul);
        zgloszenie.setOpis("Test opis");
        zgloszenie.setStatus(ZgloszenieStatus.NEW);
        zgloszenie.setDzial(dzial);
        zgloszenie.setAutor(autor);
        zgloszenie.setCreatedAt(java.time.LocalDateTime.now());
        zgloszenie.setUpdatedAt(java.time.LocalDateTime.now());
        return zgloszenieRepository.save(zgloszenie);
    }
}