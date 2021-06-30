package mayproject.news.controllers;

import mayproject.news.Services.UserService;
import mayproject.news.entities.Categories;
import mayproject.news.entities.News;
import mayproject.news.entities.Roles;
import mayproject.news.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String adminhome(Model model){
        Roles role_user = userService.findRoleByName("ROLE_USER");
        Roles role_moderator = userService.findRoleByName("ROLE_MODERATOR");
        List<Users> users = userService.findAllByRoles(role_user);
        List<Users> moderators = userService.findAllByRoles(role_moderator);
        List<News> news = userService.findAllByNews();
        model.addAttribute("moderators",moderators);
        model.addAttribute("users",users);
        model.addAttribute("news",news);
        return "admin";
    }

    @GetMapping(value = "/moderator/settings/{Url}")
    public String getModerator(Model model,
                               @PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            List<Roles> roles = userService.findAllByRoles();
            roles.removeAll(checkUser.getRoles());
            model.addAttribute("moderator",checkUser);
            model.addAttribute("roles",roles);
            return "adminsettings";
        }else{
            return "redirect:/admin/";
        }

    }

    @PostMapping(value = "/user/settings/delete/{Url}")
    public String deleteUser(@PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            userService.deleteUser(checkUser);
            return "redirect:/moderator/";
        }else{
            return "redirect:/admin/user/settings/"+url;
        }
    }
    @PostMapping(value = "/news/delete")

    public String postnewsdelete(@RequestParam(name = "news_id")Long id){
        News news = userService.findByNewsId(id);
        if(news!=null){
            userService.deleteNews(news);
            return "redirect:/admin/";
        }else{
            return "redirect:/admin/";
        }
    }

    @PostMapping(value = "/user/settings/update/{Url}")
    public String userupdate(@PathVariable(name = "Url")String Url,
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
            return "redirect:/admin/user/settings/"+user_url;
        }else{
            return "redirect:/admin/";
        }
    }

    @PostMapping(value = "/moderator/settings/update/{Url}")
    public String moderatorupdate(@PathVariable(name = "Url")String Url,
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
            return "redirect:/admin/moderator/settings/"+user_url;
        }else{
            return "redirect:/admin/moderator/settings/"+user_url+"?urlzaregistirovan";
        }
    }

    @PostMapping(value = "/moderator/settings/delete/{Url}")
    public String deleteModerator(@PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
             userService.deleteUser(checkUser);
            return "redirect:/moderator/settings/"+url;
        }else{
            return "redirect:/admin/";
        }
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
                    return "redirect:/admin/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/admin/news/settings/"+news_id;
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
                    return "redirect:/admin/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/admin/news/settings/"+news_id;
    }


    @PostMapping(value = "/user/removerole")
    public String userremoverole(@RequestParam(name = "user_url")String url,
                                 @RequestParam(name = "role_id")Long role_id,
                                 Model model){

        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            Roles role = userService.findByIdRole(role_id);
            if(role!=null){

                List<Roles>roles=checkUser.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                if(roles.contains(role)) {
                    roles.remove(role);
                    checkUser.setRoles(roles);
                    userService.saveUser(checkUser);
                    return "redirect:/admin/user/settings/" + url;
                }
            }
        }
        return "redirect:/admin/user/settings/"+url;
    }
    @PostMapping(value = "/user/addrole")
    public String useraddrole(@RequestParam(name = "user_url")String url,
                              @RequestParam(name = "role_id")Long role_id,
                              Model model){

        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            Roles role = userService.findByIdRole(role_id);
            if(role!=null){
                List<Roles>roles=checkUser.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                if(!roles.contains(role)) {
                    roles.add(role);
                    checkUser.setRoles(roles);
                    userService.saveUser(checkUser);
                    return "redirect:/admin/user/settings/" + url;
                }
            }
        }
        return "redirect:/admin/user/settings/"+url;
    }
    @PostMapping(value = "/moderator/removerole")
    public String moderatorremoverole(@RequestParam(name = "user_url")String url,
                             @RequestParam(name = "role_id")Long role_id,
                             Model model){

        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            Roles role = userService.findByIdRole(role_id);
            if(role!=null){

                List<Roles>roles=checkUser.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                if(roles.contains(role)) {
                    roles.remove(role);
                    checkUser.setRoles(roles);
                    userService.saveUser(checkUser);
                    return "redirect:/admin/moderator/settings/" + url;
                }
            }
        }
        return "redirect:/admin/moderator/settings/"+url;
    }
    @PostMapping(value = "/moderator/addrole")
    public String moderatoraddrole(@RequestParam(name = "user_url")String url,
                          @RequestParam(name = "role_id")Long role_id,
                          Model model){

        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            Roles role = userService.findByIdRole(role_id);
            if(role!=null){
                List<Roles>roles=checkUser.getRoles();
                if(roles==null){
                    roles = new ArrayList<>();
                }
                if(!roles.contains(role)) {
                    roles.add(role);
                    checkUser.setRoles(roles);
                    userService.saveUser(checkUser);
                    return "redirect:/admin/moderator/settings/" + url;
                }
            }
        }
        return "redirect:/admin/moderator/settings/"+url;
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
            return "adminnewssettings";
        }else{
            return "redirect:/admin/";
        }
    }

    @GetMapping(value = "/user/settings/{Url}")
    public String adminusersettings(Model model,
                                    @PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(checkUser!=null){
            List<Roles> roles = userService.findAllByRoles();
            roles.removeAll(checkUser.getRoles());
           model.addAttribute("user",checkUser);
           model.addAttribute("roles",roles);
           return "adminusersettings";
        }else{
            return "redirect:/admin/";
        }
    }
}
