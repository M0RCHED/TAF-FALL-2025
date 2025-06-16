package ca.etsmtl.taf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ca.etsmtl.taf.repository.RoleRepository;
import ca.etsmtl.taf.entity.ERole;
import ca.etsmtl.taf.entity.Role;
import java.util.Optional;

@SpringBootApplication
public class TestAutomationFrameworkApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(TestAutomationFrameworkApplication.class, args);
	}

	// MES MODIFICATION DEBUT
	 @Override
    public void run(String... args) {
        System.out.println("Running startup script...");
        // ✅ Place your script execution logic here
        this.createRoles();
    }

	//Create roles if not exits
	private void createRoles(){
        Optional<Role> userRoleExist = roleRepository.findByName(ERole.ROLE_USER);
        if(userRoleExist.isEmpty()){
            Role toSave = new Role(ERole.ROLE_USER);
            roleRepository.save(toSave);
        }

        userRoleExist = roleRepository.findByName(ERole.ROLE_ADMIN);
        if(userRoleExist.isEmpty()){
            Role toSave = new Role(ERole.ROLE_ADMIN);
            roleRepository.save(toSave);
        }
	}
	// MES MODIFICATION FIN

}
