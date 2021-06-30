package mayproject.news.repositories;

import mayproject.news.entities.Categories;
import mayproject.news.entities.Cities;
import mayproject.news.entities.News;
import mayproject.news.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface NewsRepository extends JpaRepository<News,Long> {
    List<News> findAllBy();
    List<News> findAllByUser(Users user);
    List<News> findAllByTitleContains(String title);
    List<News> findAllByCities(Cities cities);
    List<News> findAllByCategories(Categories categories);

}
