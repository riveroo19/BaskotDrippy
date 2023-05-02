package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.queries.QuerySearchUser;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    QuerySearchUser querySearchUser;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        userService.createUser(new User("Riverrut", "Javier Rivero", "r1ver00t development :D", passwordEncoder.encode("12345secure"), "riverrut@gmail.com",roles));
        userService.createUser(new User("Mr.Shark", "Iván Márquez", "biskot babadingshit", passwordEncoder.encode("12345secure"), "chark@mail.com",roles ));
        userService.createUser(new User("jvalserac", "Javier Valsera", "ª casabillhardt", passwordEncoder.encode("12345secure"), "quepazatigre@email.com", roles));
    }

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }

    /*@PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password){
        User user = this.userService.getUserById(username);
        System.out.println(user.getPassword());
        if (user==null || !passwordEncoder.matches(password, user.getPassword())){
            return "redirect:/error";
            //return "notRegistered"; not implemented, so we return error instead
        }
        String returnValue = "redirect:/users/" + username;
        return returnValue;
    }*/

    @GetMapping("/register")
    public String showRegister(){
        return "signup";
    }

    @PostMapping("/register") //redirected here when press register button
    public String createUser(@RequestParam("username") String username, @RequestParam("email") String email,
                             @RequestParam("fullname") String fullname,  @RequestParam("bio") String bio,  @RequestParam("password") String password, @RequestParam("confirm") String confirm){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!password.equals(confirm) || username.length()==0 && !auth.isAuthenticated()){
            return "redirect:/error";
        }else {
            User user = this.userService.createUser(new User(username,fullname,bio,password,email));
            if (user==null){
                return "redirect:/error";
            }
            return "redirect:/users/" + user.getUsername();
        }

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
