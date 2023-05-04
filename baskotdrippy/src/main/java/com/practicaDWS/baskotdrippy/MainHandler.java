package com.practicaDWS.baskotdrippy;

import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.queries.QuerySearchGarment;
import com.practicaDWS.baskotdrippy.queries.QuerySearchOutfit;
import com.practicaDWS.baskotdrippy.queries.QuerySearchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainHandler {

    @Autowired
    QuerySearchUser querySearchUser;
    @Autowired
    QuerySearchGarment querySearchGarment;
    @Autowired
    QuerySearchOutfit querySearchOutfit;

    @GetMapping("/")
    public String index(){
        return "/home";
    }

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/search")
    public String generalSearch(Model model, @RequestParam() String search){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("users", this.querySearchUser.searchByWord(search));
        }else {
            List<User> users = this.querySearchUser.searchByWord(search);
            List<User> filteredUsers = new ArrayList<>();
            for (User user: users){
                if (!user.getRoles().contains("ADMIN")){
                    filteredUsers.add(user);
                }
            }
            model.addAttribute("users", filteredUsers);
        }
        model.addAttribute("outfits", this.querySearchOutfit.searchByWord(search));
        model.addAttribute("garments", this.querySearchGarment.searchByWord(search));

        return "showEverything";
    }



}
