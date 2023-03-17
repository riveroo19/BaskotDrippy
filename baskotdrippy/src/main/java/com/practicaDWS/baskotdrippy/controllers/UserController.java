package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String getUsers(Model model){

        model.addAttribute("users", this.userService.getUsers());

        return "showUsers";
    }

    @GetMapping("/users/{username}") //show any user details
    public String getUserById(Model model, @PathVariable("username") String username){
        User user = this.userService.getUserById(username);
        if (user==null){
            return "redirect:/error.html";
        }
        model.addAttribute("user", user);
        model.addAttribute("outfits", user.getCreatedOutfits().values());
        return "prueba1";
    }

    @GetMapping("/users/deleteUser/{username}")
    public String deleteUser(Model model, @PathVariable("username") String username){
        User user = this.userService.deleteUser(username);
        if (user==null){
            return "error.html";
        }
        return "deletionSuccess";
    }

}
