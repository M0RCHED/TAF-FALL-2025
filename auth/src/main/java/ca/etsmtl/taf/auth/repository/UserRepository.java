package ca.etsmtl.taf.auth.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import ca.etsmtl.taf.auth.entity.User;

public interface UserRepository extends MongoRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

}
