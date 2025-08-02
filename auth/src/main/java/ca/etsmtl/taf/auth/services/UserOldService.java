package ca.etsmtl.taf.auth.services;
import ca.etsmtl.taf.auth.entity.ERole;
import ca.etsmtl.taf.auth.entity.Role;
import ca.etsmtl.taf.auth.entity.User;
import ca.etsmtl.taf.auth.payload.request.SignupRequest;
import ca.etsmtl.taf.auth.repository.RoleRepository;
import ca.etsmtl.taf.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserOldService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(SignupRequest signUpRequest) {
        // Create new user's account
        User user = new User(signUpRequest.getFullName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {

                ERole currentRoleType = ERole.ROLE_USER;

                if ("admin".equals(role)) {
                    currentRoleType = ERole.ROLE_ADMIN;
                }

                Role currentRole = roleRepository.findByName(currentRoleType)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                roles.add(currentRole);
            });
        }

        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
