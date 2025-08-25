package drimer.drimain.config;

import drimer.drimain.model.Role;
import drimer.drimain.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<String> base = List.of("ROLE_ADMIN","ROLE_USER","ROLE_MAGAZYN");
        base.forEach(n ->
                roleRepository.findByName(n).orElseGet(() -> roleRepository.save(new Role(n)))
        );
    }
}