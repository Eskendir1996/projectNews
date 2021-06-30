package mayproject.news.Services.impl;

import mayproject.news.Services.UserService;
import mayproject.news.entities.*;
import mayproject.news.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;



    @Override
    public Categories findByCategoryName(String categoryName) {
        return categoriesRepository.findByName(categoryName);
    }

    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private CategoriesRepository categoriesRepository;
    @Override
    public Users checkUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Roles findRoleByName(String role) {
        return rolesRepository.findByRole(role);
    }

    @Override
    public void addUser(Users user) {
         userRepository.save(user);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Users user) {
      userRepository.delete(user);
    }

    @Override
    public Users findByUserurl(String userUrl) {
        return userRepository.findByUserurl(userUrl);
    }

    @Override
    public List<Users> findAllByRoles(Roles role) {
        return userRepository.findAllByRoles(role);
    }

    @Override
    public List<News> findAllByUser(Users user) {
        return newsRepository.findAllByUser(user);
    }

    @Override
    public News findByNewsId(Long id) {
        return newsRepository.findById(id).orElse(null);
    }


    @Override
    public void addNews(News news) {
       newsRepository.save(news);
    }

    @Override
    public void deleteNews(News news) {
     newsRepository.delete(news);
    }

    @Override
    public List<News> findAllByNews() {
        return newsRepository.findAllBy();
    }

    @Override
    public List<Cities> findAllByCities() {
        return citiesRepository.findAllBy();
    }

    @Override
    public List<News> findAllByNewscity(Cities city) {
        return newsRepository.findAllByCities(city);
    }

    @Override
    public Cities findByCityId(Long id) {
        return citiesRepository.findById(id).orElse(null);
    }

    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
    }

    @Override
    public Roles findByIdRole(Long id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    public List<News> findAllNewsContainsTitle(String title) {
        return newsRepository.findAllByTitleContains(title);
    }

    @Override
    public List<Roles> findAllByRoles() {
        return rolesRepository.findAll();
    }

    @Override
    public Categories findByCategoryId(Long id) {
        return categoriesRepository.findById(id).orElse(null);
    }

    @Override
    public List<Categories> findAllCategories() {
        return categoriesRepository.findAllBy();
    }

    @Override
    public Cities findByCityName(String city_name) {
        return citiesRepository.findByName(city_name);
    }

    @Override
    public List<News> findAllByNewsCategory(Categories category) {
        return newsRepository.findAllByCategories(category);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(email);
        if(user!=null){
            return user;
        }else {
            throw new UsernameNotFoundException("not found exception");
        }
    }
}
