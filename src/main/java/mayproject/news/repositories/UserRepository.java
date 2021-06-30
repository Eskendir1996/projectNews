package mayproject.news.repositories;

import mayproject.news.entities.Roles;
import mayproject.news.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByEmail(String email);
    Users findByUserurl(String userUrl);
    List<Users> findAllByRoles(Roles role);
}
