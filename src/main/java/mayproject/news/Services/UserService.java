package mayproject.news.Services;

import mayproject.news.entities.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    Users checkUser(String email);
    Roles findRoleByName(String role);
    void addUser(Users user);
    Users saveUser(Users user);
    void deleteUser(Users user);
    Users findByUserurl(String userUrl);
    List<Users>findAllByRoles(Roles role);
    List<News> findAllByUser(Users user);
    News findByNewsId(Long id);
    void addNews(News news);
    void deleteNews(News news);
     List<News>findAllByNews();
     List<Cities>findAllByCities();
     Cities findByCityId(Long id);
     void saveNews(News news);
     List<Categories> findAllCategories();
     Categories findByCategoryId(Long id);
     Categories findByCategoryName(String categoryName);
     List<Roles>findAllByRoles();
     Roles findByIdRole(Long id);
     List<News> findAllNewsContainsTitle(String title);
     List<News> findAllByNewsCategory(Categories category);
     Cities findByCityName(String city_name);
     List<News> findAllByNewscity(Cities city);
}
