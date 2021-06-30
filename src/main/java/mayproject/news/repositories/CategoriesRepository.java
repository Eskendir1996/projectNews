package mayproject.news.repositories;

import mayproject.news.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoriesRepository extends JpaRepository<Categories,Long> {
     List<Categories>findAllBy();
     Categories findByName(String category_name);
}

