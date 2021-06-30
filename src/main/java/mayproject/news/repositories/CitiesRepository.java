package mayproject.news.repositories;

import mayproject.news.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CitiesRepository extends JpaRepository<Cities,Long> {
    List<Cities> findAllBy();
    Cities findByName(String city_name);
}
