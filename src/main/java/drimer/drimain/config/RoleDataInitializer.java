package drimer.drimain.config;

import drimer.drimain.model.Role;
import drimer.drimain.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleDataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        addIfMissing("ADMIN");
        addIfMissing("USER");
    }

    private void addIfMissing(String name) {
        roleRepository.findByName(name).orElseGet(() -> roleRepository.save(new Role(name)));
    }
}