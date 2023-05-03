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
import java.util.Collection;
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
        userService.createUser(new User("Riverrut", "Javier Rivero", "r1ver00t development :D", "12345secure", "riverrut@gmail.com",roles));
        userService.createUser(new User("Mr.Shark", "Iván Márquez", "biskot babadingshit", "12345secure", "chark@mail.com",roles ));
        userService.createUser(new User("jvalserac", "Javier Valsera", "ª casabillhardt", "12345secure", "quepazatigre@email.com", roles));
    }

    @GetMapping("/login")
    public String showLogin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            return "redirect:/users/" + auth.getName();
        }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            return "redirect:/users/" + auth.getName();
        }
        return "signup";
    }

    @PostMapping("/register") //redirected here when press register button
    public String createUser(@RequestParam("username") String username, @RequestParam("email") String email,
                             @RequestParam("fullname") String fullname,  @RequestParam("bio") String bio,  @RequestParam("password") String password, @RequestParam("confirm") String confirm){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!password.equals(confirm) || username.length()==0 && auth.isAuthenticated()){
            return "redirect:/error";
        }else {
            User user = this.userService.createUser(new User(username,fullname,bio,password,email));
            if (user==null){//already exists
                return "redirect:/error";
            }
            return "redirect:/users/" + user.getUsername();
        }

    }

    //user functionalities

    @GetMapping("/users")
    public String getUsers(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("users", this.userService.getUsers());
        }else {
            model.addAttribute("users", this.querySearchUser.findAllNonAdminUsers());
        }

        return "showUsers";
    }

    @GetMapping("/users/{username}") //show any user details
    public String getUserById(Model model, @PathVariable("username") String username){
        User user = this.userService.getUserById(username);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user==null || (username.equals("admin") && auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))){//if doesnt exist or the one trying to access on
            // admin's profile is not admin
            return "redirect:/error";
        }else {
            boolean isSameUser = auth.getName().equals(user.getUsername()) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")); //if he's admin, he can do whatever
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("isSameUser", isSameUser);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("fullname", user.getFullname());
            model.addAttribute("isSameUser", isSameUser);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("bio", user.getBio());
            model.addAttribute("email", user.getEmail());
            if(isSameUser){
                model.addAttribute("outfitsSameUser", user.getCreatedOutfits());
            }else {
                model.addAttribute("outfits", user.getCreatedOutfits());
            }
            model.addAttribute("isSameUser", isSameUser);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("isSameUser", isSameUser);
            return "detailUser";
        }

    }

    @GetMapping("/users/deleteUser/{username}")
    public String deleteUser(@PathVariable("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getName().equals("admin")){
            if (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){//if it's admin or the user trying to delete is the same as the specified
                User user = this.userService.deleteUser(username);
                if (user==null){
                    return "redirect:/error";
                }
                return "redirect:/logout";
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/deleteUsers")
    public String deleteUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            this.userService.deleteUsers();
            return "redirect:/users";
        }
        return "redirect:/error";
    }



    @GetMapping("/users/updateUser/{username}")
    public String updateUser(Model model, @PathVariable("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){//if it's admin or the user trying to update is the same as the specified
            User user = this.userService.getUserById(username);
            model.addAttribute("user", user);
            return "updateUser";
        }
        return "redirect:/error";
    }

    @PostMapping("/users/modifySuccess")
    public String modifySuccess(@RequestParam("username") String username, @RequestParam("email") String email, @RequestParam("fullname") String fullname,
                                @RequestParam("bio") String bio,  @RequestParam("password") String password, @RequestParam("confirm") String confirm, @RequestParam("confirm1") String confirm1){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            if (!confirm.equals(confirm1) || !passwordEncoder.matches(password, this.userService.getUserById(username).getPassword())){
                return "redirect:/error";
            }else {
                User user = this.userService.modifyUser(username, new User(username, fullname, bio, confirm, email));
                if (user==null){
                    return "redirect:/error";
                }
                String returnvalue = "redirect:/users/" + username;
                return returnvalue;
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/users/{username}/createOutfit")
    public String createOutfit(Model model, @PathVariable("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("username", username);
            return "createOutfit";
        }
        return "redirect:/error";
    }

    @GetMapping("/searchUsers")
    public String searchUser(Model model, @RequestParam String search){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("users", this.querySearchUser.searchByWord(search));
            return "showUsers";
        }else if (auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken) && auth!=null){
            List<User> users = this.querySearchUser.searchByWord(search);
            List<User> filteredUsers = new ArrayList<>();
            for (User user: users){
                if (!user.getRoles().contains("ADMIN")){
                    filteredUsers.add(user);
                }
            }
            model.addAttribute("users", filteredUsers);
            return "showUsers";
        }
        return "redirect:/error";
    }

}
