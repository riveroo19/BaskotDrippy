package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    //pseudo login-related
    @PostMapping("/login")
    public String pseudoLogin(Model model, @RequestParam("username") String username){
        User user = this.userService.getUserById(username);
        if (user==null){
            return "notRegistered";
        }
        return "redirect:/users/{username}";
    }

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
        return "showUser";
    }

    @GetMapping("/users/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username){
        User user = this.userService.deleteUser(username);
        if (user==null){
            return "redirect:/error.html";
        }
        return "deletionSuccess";
    }

    @GetMapping("/users/createUser") //redirected here when press register button
    public String createUser(@RequestParam("username") String username, @RequestParam("fullname") String fullname,
         @RequestParam("email") String email,  @RequestParam("bio") String bio,  @RequestParam("password") String password /*confirmpass*/){
        User user = new User(username,fullname,bio,password,email);
        user = this.userService.createUser(user);
        if (user==null){
            return "redirect:error";
        }
        return "redirect:/users/{username}";
    }

}
