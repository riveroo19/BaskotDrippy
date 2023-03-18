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

    //extra functionalities
    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/login")
    public String pseudoLogin(Model model, @RequestParam("username") String username, @RequestParam("password") String password){
        User user = this.userService.getUserById(username);
        if (user==null || !user.getPassword().equals(password)){
            return "redirect:/error";
            //return "notRegistered";
        }
        return this.getUserById(model, username);
    }

    //user functionalities

    @GetMapping("/users")
    public String getUsers(Model model){

        model.addAttribute("users", this.userService.getUsers());

        return "showUsers";
    }

    @GetMapping("/users/{username}") //show any user details
    public String getUserById(Model model, @PathVariable("username") String username){
        User user = this.userService.getUserById(username);
        if (user==null){
            return "redirect:/error";
        }
        model.addAttribute("user", user);
        model.addAttribute("outfits", user.getCreatedOutfits().values().stream().toList());
        return "detailUser";
    }

    @GetMapping("/users/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username){
        User user = this.userService.deleteUser(username);
        if (user==null){
            return "redirect:/error";
        }
        return "deletionSuccess";
    }

    @PostMapping("/users/createUser") //redirected here when press register button
    public String createUser(@RequestParam("username") String username, @RequestParam("email") String email,
         @RequestParam("fullname") String fullname,  @RequestParam("bio") String bio,  @RequestParam("password") String password, @RequestParam("confirm") String confirm){
        User user = this.userService.createUser(new User(username,fullname,bio,password,email));
        if (user==null){
            return "redirect:/error";
        }
        return "redirect:/users";
    }



}
