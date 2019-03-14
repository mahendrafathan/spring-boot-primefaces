package prosia.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import prosia.app.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
