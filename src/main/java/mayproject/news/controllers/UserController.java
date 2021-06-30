package mayproject.news.controllers;

import mayproject.news.Services.UserService;
import mayproject.news.entities.Categories;
import mayproject.news.entities.Cities;
import mayproject.news.entities.News;
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
@PreAuthorize("isAuthenticated()")
@RequestMapping("/user")
public class UserController {
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
        for(int i=1;i<news.size();i++){
            if(i>6){
                news.remove(news.get(i));
            }
        }
        model.addAttribute("news",news);
        return "index";
    }

    @PostMapping(value = "/news/updatenews")
    public String postupdatenews(@RequestParam(name = "news_title")String news_title,
                                 @RequestParam(name = "news_text")String news_text,
                                 @RequestParam(name = "news_image")String news_image,
                                 @RequestParam(name = "news_id")Long news_id){
        News checkNews = userService.findByNewsId(news_id);
        if(checkNews!=null){
            checkNews.setTitle(news_title);
            checkNews.setText(news_text);
            checkNews.setImage(news_image);
            userService.saveNews(checkNews);
            return "redirect:/user/news/settings/"+news_id;
        }else{
            return "redirect:/user/news/settings/"+news_id+"?error";
        }
    }

    @PostMapping(value = "/news/delete")
    public String postdeletenews(@RequestParam(name = "news_id")Long news_id){
        News checkNews = userService.findByNewsId(news_id);
        if(checkNews!=null&&getUser().getUserurl().equals(checkNews.getUser().getUserurl())){
            userService.deleteNews(checkNews);
            return "/menu/";
        }else{
            return "redirect:/user/news/settings/"+news_id+"?error";
        }
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("CURRENT_USER",getUser());
        List<News> UserNews = userService.findAllByUser(getUser());
        model.addAttribute("news",UserNews);
        return "profile";
    }

    @GetMapping(value = "/news/settings/{NewsId}")
    public String updateUserSettingsNews(Model model,
                                         @PathVariable(name = "NewsId")Long id){
        News news = userService.findByNewsId(id);
        if(news!=null) {
            if (news.getUser().getUserurl().equals(getUser().getUserurl())) {
                model.addAttribute("news", news);
                List<Categories> categories = userService.findAllCategories();
                categories.removeAll(news.getCategories());
                model.addAttribute("categories", categories);
                return "userupdatenews";
            } else {
                return "redirect:/user/profile";
            }
        }else{
            return"redirect:/user/profile";
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
                    return "redirect:/user/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/user/news/settings/"+news_id;
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
                    return "redirect:/user/news/settings/" + news_id;
                }
            }
        }
        return "redirect:/user/news/settings/"+news_id;
    }



    @GetMapping(value = "/updateprofile/{UserUrl}")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(Model model,@PathVariable(name = "UserUrl")String UserUrl){
        Users user = userService.findByUserurl(UserUrl);
        if(user!=null&&user.getUserurl().equals(getUser().getUserurl())) {
            model.addAttribute("CURRENT_USER", getUser());
            return "updateprofile";
        }else{
            return "redirect:/profile/"+UserUrl;
        }
    }

    @PostMapping(value = "/updateprofile")
    public String updateprofile(Model model, @RequestParam(name = "old_password")String old_password,
                                @RequestParam(name = "new_password")String new_password,
                                @RequestParam(name = "re_new_password")String re_new_password,
                                @RequestParam(name = "user_url")String user_url,
                                @RequestParam(name = "user_name")String name,
                                @RequestParam(name = "user_surname")String surname,
                                @RequestParam(name = "picture_url")String picture_url){
        Users currentUser = getUser();
        Users UrlUser = userService.findByUserurl(user_url);
        if (passwordEncoder.matches(old_password.trim(),currentUser.getPassword())) {
            if(UrlUser==null||currentUser.getUserurl().equals(UrlUser.getUserurl())) {
                if (new_password.trim().equals(re_new_password.trim())) {
                    currentUser.setPassword(passwordEncoder.encode(new_password));
                    currentUser.setUserurl(user_url);
                    currentUser.setName(name);
                    currentUser.setSurname(surname);
                    currentUser.setPictureurl(picture_url);
                    userService.saveUser(currentUser);
                    model.addAttribute("CURRENT_USER",currentUser);
                    return "redirect:/user/profile";
                } else {
                    return "redirect:/user/updateprofile/"+currentUser.getUserurl()+"?newpassworderror";
                }
            }else{
                return "redirect:/user/updateprofile/"+currentUser.getUserurl()+"?userurlzaregistirovan";
            }
        }else{
            return "redirect:/user/updateprofile/"+currentUser.getUserurl()+"?oldpassworderror";
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
