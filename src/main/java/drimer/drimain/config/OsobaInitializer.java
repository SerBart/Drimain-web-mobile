package drimer.drimain.config;

import drimer.drimain.model.Osoba;
import drimer.drimain.repository.OsobaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(20) // Execute after other initializers
public class OsobaInitializer implements ApplicationRunner {

    private final OsobaRepository osobaRepository;
    private final PasswordEncoder passwordEncoder;

    public OsobaInitializer(OsobaRepository osobaRepository, 
                           PasswordEncoder passwordEncoder) {
        this.osobaRepository = osobaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Create test admin user if doesn't exist
        osobaRepository.findByLogin("admin").orElseGet(() -> {
            Osoba admin = new Osoba();
            admin.setLogin("admin");
            admin.setHaslo(passwordEncoder.encode("admin123"));
            admin.setImieNazwisko("Administrator Systemu");
            admin.setRola("ADMIN");
            return osobaRepository.save(admin);
        });

        // Create test regular user if doesn't exist
        osobaRepository.findByLogin("user").orElseGet(() -> {
            Osoba user = new Osoba();
            user.setLogin("user");
            user.setHaslo(passwordEncoder.encode("user123"));
            user.setImieNazwisko("Zwykły Użytkownik");
            user.setRola("USER");
            return osobaRepository.save(user);
        });
    }
}