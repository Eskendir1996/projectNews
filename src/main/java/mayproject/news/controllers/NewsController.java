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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/addnews")
    public String getaddnews(Model model){
        if(getUser()!=null) {
            List<Cities> cities = userService.findAllByCities();
            model.addAttribute("cities", cities);
            return "addnews";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping(value = "/category")
    public String getnewscategory(Model model,@RequestParam(name = "category")String category_name){
        List<Categories> categories = userService.findAllCategories();
        model.addAttribute("categories",categories);
        List<Cities> cities = userService.findAllByCities();
        model.addAttribute("cities",cities);
        Categories category = userService.findByCategoryName(category_name);
        if(category!=null) {
            List<News> category_news = userService.findAllByNewsCategory(category);
            model.addAttribute("news", category_news);
            return "index";
        }else{
            return "redirect:/";
        }
    }
    @GetMapping(value = "/cities")
    public String getnewscity(Model model,@RequestParam(name = "city")String city_name){
        List<Categories> categories = userService.findAllCategories();
        model.addAttribute("categories",categories);
        List<Cities> cities = userService.findAllByCities();
        model.addAttribute("cities",cities);
        Cities city = userService.findByCityName(city_name);
        if(city!=null) {
            List<News> city_news = userService.findAllByNewscity(city);
            model.addAttribute("news", city_news);
            return "index";
        }else{
            return "redirect:/";
        }
    }

    @PostMapping(value = "/addnews")
    public String postaddnews(Model model,
                              @RequestParam(name = "title")String title,
                              @RequestParam(name = "text")String text,
                              @RequestParam(name = "image")String image,
                              @RequestParam(name = "city_id")Long city_id){
        Cities city = userService.findByCityId(city_id);
        if(city!=null&&title!=null&&text!=null&&image!=null){
            News newNews = new News();
            newNews.setTitle(title);
            newNews.setText(text);
            newNews.setImage(image);
            newNews.setCities(city);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            newNews.setTime_date(timestamp);
            newNews.setUser(getUser());
            userService.addNews(newNews);
            return "redirect:/user/";
        }else{
            return "redirect:/news/addnews?error";
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
