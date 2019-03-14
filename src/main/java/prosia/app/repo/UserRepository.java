package prosia.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import prosia.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	   User findByUsername(String username);
}
