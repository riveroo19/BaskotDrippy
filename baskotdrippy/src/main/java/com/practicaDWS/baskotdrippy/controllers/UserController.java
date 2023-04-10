package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.queries.QuerySearchUser;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    QuerySearchUser querySearchUser;

    //extra functionalities

    @GetMapping("/login") //this only checks if the user exists, only decoration for the web
    public String pseudoLogin(@RequestParam("username") String username, @RequestParam("password") String password){
        User user = this.userService.getUserById(username);
        if (user==null || !user.getPassword().equals(password)){
            return "redirect:/error";
            //return "notRegistered"; not implemented, so we return error instead
        }
        String returnValue = "redirect:/users/" + username;
        return returnValue;
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
        model.addAttribute("outfits", user.getCreatedOutfits());
        model.addAttribute("username", user.getUsername());
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
        if (!password.equals(confirm) || username.length()==0){
            return "redirect:/error";
        }else {
            User user = this.userService.createUser(new User(username,fullname,bio,password,email));
            if (user==null){
                return "redirect:/error";
            }
            return "redirect:/users";
        }

    }

    @GetMapping("/users/updateUser/{username}")
    public String updateUser(Model model, @PathVariable("username") String username){
        User user = this.userService.getUserById(username);
        model.addAttribute("user", user);
        return "updateUser";
    }

    @PostMapping("/users/modifySuccess")
    public String modifySuccess(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("fullname") String fullname,
                                @RequestParam("bio") String bio,  @RequestParam("password") String password, @RequestParam("confirm") String confirm, @RequestParam("confirm1") String confirm1){
        if (!confirm.equals(confirm1) || !password.equals(this.userService.getUserById(username).getPassword())){
            return "redirect:/error";
        }else {
            User user = this.userService.modifyUser(username, new User(username, fullname, bio, password, email));
            if (user==null){
                return "redirect:/error";
            }
            String returnvalue = "redirect:/users/" + username;
            return returnvalue;
        }
    }

    @GetMapping("/users/{username}/createOutfit")
    public String createOutfit(Model model, @PathVariable("username") String username){
        model.addAttribute("username", username);
        return "createOutfit";
    }

    @GetMapping("/searchUsers")
    public String searchUser(Model model, @RequestParam String search){
        model.addAttribute("users", this.querySearchUser.searchByWord(search));
        return "showUsers";
    }



}
