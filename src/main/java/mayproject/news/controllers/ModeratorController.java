package mayproject.news.controllers;

import lombok.experimental.PackagePrivate;
import mayproject.news.Services.UserService;
import mayproject.news.entities.Categories;
import mayproject.news.entities.News;
import mayproject.news.entities.Roles;
import mayproject.news.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MODERATOR')")
@RequestMapping(value = "/moderator")
public class ModeratorController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping(value = "/")
    public String home(Model model){

        List<News> AllNews = userService.findAllByNews();
        model.addAttribute("news",AllNews);

        Roles role = userService.findRoleByName("ROLE_USER");
        List<Users> users = userService.findAllByRoles(role);
        model.addAttribute("users",users);
        return "moderator";
    }
    @PostMapping(value = "/news/removecategory")
    public String removecategory(@RequestParam(name = "news_id")Long news_id,
                                 @RequestParam(name = "category_id")Long category_id,
                                 Model model){

        News news = userService.findByNewsId(news_id);
        if(news!=null){
            Categories category = userService.findByCategoryId(category_id);
            if(category!=null){

                List<Categories>categories=news.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                if(categories.contains(category)) {
                    categories.remove(category);
                    news.setCategories(categories);
                    userService.saveNews(news);
                    return "redirect:/moderator/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/moderator/news/settings/"+news_id;
    }
    @PostMapping(value = "/news/addcategory")
    public String addcategory(@RequestParam(name = "news_id")Long news_id,
                              @RequestParam(name = "category_id")Long category_id,
                              Model model){

        News news = userService.findByNewsId(news_id);
        if(news!=null){
            Categories category = userService.findByCategoryId(category_id);
            if(category!=null){

                List<Categories>categories=news.getCategories();
                if(categories==null){
                    categories = new ArrayList<>();
                }
                if(!categories.contains(category)) {
                    categories.add(category);
                    news.setCategories(categories);
                    userService.saveNews(news);
                    return "redirect:/moderator/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/moderator/news/settings/"+news_id;
    }

    @GetMapping(value = "/news/settings/{NewsId}")
    public String getupdateNews(Model model,
                             @PathVariable(name = "NewsId")Long id){
        News news = userService.findByNewsId(id);
        if(news!=null) {
            model.addAttribute("news", news);
            List<Categories> categories = userService.findAllCategories();
            categories.removeAll(news.getCategories());
            model.addAttribute("categories", categories);
            return "updatenews";
        }else{
            return "redirect:/moderator/";
        }
    }

    @PostMapping(value = "/user/settings/delete/{Url}")
    public String deleteUser(@PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            userService.deleteUser(checkUser);
            return "redirect:/moderator/";
        }else{
            return "redirect:/moderator/user/settings/"+url;
        }
    }
    @PostMapping(value = "/news/delete")
    public String deleteNews(@RequestParam(name = "news_id")Long id){
        News checkUser = userService.findByNewsId(id);
        if(checkUser!=null) {
            userService.deleteNews(checkUser);
            return "redirect:/moderator/";
        }else{
            return "redirect:/moderator/";
        }
    }

    @PostMapping(value = "/news/updatenews")
    public String postupdateNews(@RequestParam(name = "news_id")Long id,
                                 @RequestParam(name = "news_title")String title,
                                 @RequestParam(name = "news_text")String text,
                                 @RequestParam(name = "news_image")String image){
        News checkNews = userService.findByNewsId(id);
        if(checkNews!=null){
            checkNews.setTitle(title);
            checkNews.setText(text);
            checkNews.setImage(image);
            userService.saveNews(checkNews);
            return "redirect:/moderator/news/settings/"+id;
        }else{
            return "redirect:/moderator/";
        }
    }

    @GetMapping(value = "/user/settings/{Url}")
    public String moderatorusersettings(Model model,
                                        @PathVariable(name = "Url")String url){
        Users user = userService.findByUserurl(url);
        if(user!=null) {
            model.addAttribute("CURRENT_USER", user);
            return "moderatorsettings";
        }else{
            return "/moderator/";
        }
    }
    @PostMapping(value = "/user/settings/update/{Url}")
    public String update(@PathVariable(name = "Url")String Url,
                         @RequestParam(name = "user_email")String email,
                         @RequestParam(name = "user_password")String password,
                         @RequestParam(name = "user_name")String name,
                         @RequestParam(name = "user_surname")String surname,
                         @RequestParam(name = "user_url")String user_url,
                         @RequestParam(name = "picture_url")String picture_url){
         Users checkUser = userService.findByUserurl(Url);
         if(checkUser!=null){
              checkUser.setEmail(email);
              checkUser.setPassword(passwordEncoder.encode(password));
              checkUser.setName(name);
              checkUser.setSurname(surname);
              checkUser.setUserurl(user_url);
              checkUser.setPictureurl(picture_url);
              userService.saveUser(checkUser);
             return "redirect:/moderator/user/settings/"+user_url;
         }else{
             return "redirect:/moderator/";
         }
    }
    public Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users user = (Users)authentication.getPrincipal();
            return user;
        }
        return null;
    }
}
