package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.queries.QuerySearchUser;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class UserRESTController {
    @Autowired
    UserService userService;
    @Autowired
    QuerySearchUser querySearchUser;
    @Autowired
    OutfitService outfitService;

    //CRUD functionalities
    @JsonView(User.UserView.class)
    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth)){
            if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                return new ResponseEntity<>(this.userService.getUsers(), HttpStatus.OK);
            }
            return new ResponseEntity<>(this.querySearchUser.findAllNonAdminUsers(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(User.UserView.class)
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserById(@PathVariable("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth)){
            User user = this.userService.getUserById(username);
            if (user==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }else if(auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else if (user.getRoles().contains("ADMIN")){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }else {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(User.UserView.class)
    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable("username") String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && !auth.getName().equals("admin")){
            if ((auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
                User user = this.userService.deleteUser(username);
                if (user!=null){
                    user.getCreatedOutfits().clear();
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //admin can not delete his own user, for security
    }

    @JsonView(User.UserView.class)
    @PatchMapping("/users/{username}")
    public ResponseEntity<User> modifyUser(@PathVariable("username") String username, @RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
            User user1 = this.userService.modifyUser(username, user);
            if (user1!=null){
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(User.UserView.class)
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!checkSession(auth)){
            User user1 = this.userService.createUser(user);
            if (user1!=null){
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); //cause there is session
    }

    //others
    //not sure if necessary (you can delete an Outfit with deleteMapping /outfits/{id})

    @JsonView(User.UserView.class)
    @PatchMapping("/users/{username}/addOutfit")
    public ResponseEntity<User> addOutfit(@PathVariable("username") String username, @RequestBody Outfit outfit){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
            if (outfit.getOwner().equals(username)){
                Outfit outfit1 = this.outfitService.createOutfit(outfit);
                if (outfit1!=null){ //the user exists
                    return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.CREATED);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(User.UserView.class)
    @PatchMapping("/users/{username}/quitOutfit/{id}")
    public ResponseEntity<User> quitOutfit(@PathVariable("username") String username, @PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
            if (this.outfitService.getOutfitById(id).getOwner().equals(username) && this.userService.getUserById(username).existOutfit(id)){
                Outfit outfit = this.outfitService.deleteOutfit(id);
                if (outfit!=null && this.userService.getUserById(username)!=null){
                    return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    //same as above
    @JsonView(User.UserView.class)
    @PatchMapping("/users/{username}/modifyOutfit/{id}")
    public ResponseEntity<User> modifyOutfit(@PathVariable("username") String username, @PathVariable("id") Long id, @RequestBody Outfit outfit){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && (auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
            if (this.outfitService.getOutfitById(id).getOwner().equals(username) && this.userService.getUserById(username).existOutfit(id)){
                Outfit outfit1 = this.outfitService.modifyOutfit(id, outfit);
                if (outfit1!=null && this.userService.getUserById(username)!=null){
                    return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(User.UserView.class)
    @DeleteMapping("/deleteUsers")
    public ResponseEntity<Collection<User>> deleteUsers(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            this.userService.deleteUsers();
            Collection<User> users = this.userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private boolean checkSession(Authentication auth){
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
