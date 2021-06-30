package mayproject.news.controllers;

import mayproject.news.Services.UserService;
import mayproject.news.entities.*;
import mayproject.news.repositories.UserRepository;
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
@RequestMapping(value = "/")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/")
    public String home(Model model){
        List<News> news = userService.findAllByNews();
        List<Categories> categories = userService.findAllCategories();
        model.addAttribute("categories",categories);
        List<Cities> cities = userService.findAllByCities();
        model.addAttribute("cities",cities);

        model.addAttribute("news",news);
        return "index";
    }

    @GetMapping(value = "/menu/")
    public String menu(Model model){
        Categories qogamcategory = userService.findByCategoryName("Қоғам");
        List<News> qogamNews = userService.findAllByNewsCategory(qogamcategory);

        model.addAttribute("qogamnews",qogamNews);

        Categories alemcategory = userService.findByCategoryName("Әлем жаңалықтары");
        List<News> alemnews = userService.findAllByNewsCategory(alemcategory);

        model.addAttribute("alemnews",alemnews);

        Categories sportcategory = userService.findByCategoryName("Спорт");
        List<News> sportnews = userService.findAllByNewsCategory(sportcategory);

        model.addAttribute("sportnews",sportnews);

        Categories otbasycategory = userService.findByCategoryName("Отбасы");
        List<News> otbasynews = userService.findAllByNewsCategory(otbasycategory);

        model.addAttribute("otbasynews",otbasynews);

        Categories qazynacategory = userService.findByCategoryName("Қазына");
        List<News> qazynanews = userService.findAllByNewsCategory(qazynacategory);

        model.addAttribute("qazynanews",qazynanews);

        Categories aleumetcategory = userService.findByCategoryName("Әлеумет");
        List<News> aleumetnews = userService.findAllByNewsCategory(aleumetcategory);

        model.addAttribute("aleumetnews",aleumetnews);

        Categories sayasatcategory = userService.findByCategoryName("Саясат пен қаржы");
        List<News> sayasatnews = userService.findAllByNewsCategory(sayasatcategory);

        model.addAttribute("sayasatnews",sayasatnews);

        Categories showbiznescategory = userService.findByCategoryName("Шоу-бизнес");
        List<News> showbiznews = userService.findAllByNewsCategory(showbiznescategory);

        model.addAttribute("showbiznews",showbiznews);

        Categories onercategory = userService.findByCategoryName("Өнер");
        List<News> onernews = userService.findAllByNewsCategory(onercategory);

        model.addAttribute("onernews",onernews);

        Categories paidalykengestercategory = userService.findByCategoryName("Пайдалы кеңестер");
        List<News> paidalykengesternews = userService.findAllByNewsCategory(paidalykengestercategory);

        model.addAttribute("paidalykengesternews",paidalykengesternews);
        return "menu";
    }


    @GetMapping(value = "/menu/news/{NewsId}")
    public String homeMenu(Model model,
                           @PathVariable(name = "NewsId")Long id) {
        List<News> newsList = new ArrayList<>();
        if (id != null) {
            News news = userService.findByNewsId(id);
            if(news!=null) {
                newsList.add(news);
                model.addAttribute("news", newsList);
                List<Categories> categories = userService.findAllCategories();
                model.addAttribute("categories",categories);
                List<Cities> cities = userService.findAllByCities();
                model.addAttribute("cities",cities);
                return "index";
            }
        }
        return "redirect:/menu/?newserror";
    }


    @GetMapping(value = "/search")
    public String search(Model model,@RequestParam(name = "key")String key){
        List<News> news = userService.findAllNewsContainsTitle(key);
        List<Categories> categories = userService.findAllCategories();
        model.addAttribute("categories",categories);
        List<Cities> cities = userService.findAllByCities();
        model.addAttribute("cities",cities);
        model.addAttribute("news", news);
        return "index";
    }

    @GetMapping(value = "/login")
    @PreAuthorize("isAnonymous()")
    public String login(){
        return "login";
    }
    @GetMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String register(){
        return "register";
    }

    @GetMapping(value = "/profile/{Url}")
    public String profileall(Model model,
                             @PathVariable(name = "Url")String url){
        Users checkUser = userService.findByUserurl(url);
        if(getUser()!=null) {
            if (!(checkUser.getUserurl().equals(getUser().getUserurl()))) {
                model.addAttribute("CURRENT_USER",checkUser);
                return "allprofile";
            } else {
                return "redirect:/user/profile";
            }
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping(value = "/register")
    public String postregister(@RequestParam(name = "user_name")String name,
                               @RequestParam(name = "user_surname")String surname,
                               @RequestParam(name = "user_email")String email,
                               @RequestParam(name = "user_password")String password,
                               @RequestParam(name = "user_url")String user_Url){
       Users urlUser = userService.findByUserurl(user_Url);
       Users checkUser = userService.checkUser(email);
       String picture_url = "/avatars/default.png";

       if(urlUser==null){
           if(checkUser==null){
               List<Roles> roles = new ArrayList<>();
               Roles role= userService.findRoleByName("ROLE_USER");
               roles.add(role);
               Users newUser = new Users();
               newUser.setName(name);
               newUser.setSurname(surname);
               newUser.setUserurl(user_Url);
               newUser.setPictureurl(picture_url);
               newUser.setPassword(passwordEncoder.encode(password));
               newUser.setRoles(roles);
               newUser.setEmail(email);
               userService.addUser(newUser);
               return "/login";
           }else{
               return "redirect:/register?emailzaregistirovan";
           }
       }else{
           return "redirect:/register?urlzaregistirovan";
       }
    }

    @GetMapping(value = "/accessdenied")
    public String accessdenied(){
     return "accessdenied";
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
