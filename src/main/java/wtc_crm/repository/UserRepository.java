package wtc_crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wtc_crm.model.User;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}