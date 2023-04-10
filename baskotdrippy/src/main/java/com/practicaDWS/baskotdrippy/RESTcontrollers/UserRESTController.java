package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import com.practicaDWS.baskotdrippy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class UserRESTController {
    @Autowired
    UserService userService;
    @Autowired
    OutfitService outfitService;

    //CRUD functionalities
    @GetMapping("/users")
    public ResponseEntity<Collection<User>> getUsers(){
        return new ResponseEntity<>(this.userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserById(@PathVariable("username") String username){
        User user = this.userService.getUserById(username);
        if (user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable("username") String username){
        User user = this.userService.deleteUser(username);
        if (user!=null){
            user.getCreatedOutfits().clear();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<User> modifyUser(@PathVariable("username") String username, @RequestBody User user){
        User user1 = this.userService.modifyUser(username, user);
        if (user1!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1 = this.userService.createUser(user);
        if (user1!=null){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }

    //others
    //not sure if necessary (you can delete an Outfit with deleteMapping /outfits/{id})

    @PatchMapping("/users/{username}/addOutfit")
    public ResponseEntity<User> addOutfit(@PathVariable("username") String username, @RequestBody Outfit outfit){
        if (outfit.getOwner().equals(username)){
            Outfit outfit1 = this.outfitService.createOutfit(outfit);
            if (outfit1!=null){ //the user exists
                return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/users/{username}/quitOutfit/{id}")
    public ResponseEntity<User> quitOutfit(@PathVariable("username") String username, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(id).getOwner().equals(username) && this.userService.getUserById(username).existOutfit(id)){
            Outfit outfit = this.outfitService.deleteOutfit(id);
            if (outfit!=null && this.userService.getUserById(username)!=null){
                return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //same as above
    @PatchMapping("/users/{username}/modifyOutfit/{id}")
    public ResponseEntity<User> modifyOutfit(@PathVariable("username") String username, @PathVariable("id") Long id, @RequestBody Outfit outfit){
        if (this.outfitService.getOutfitById(id).getOwner().equals(username) && this.userService.getUserById(username).existOutfit(id)){
            Outfit outfit1 = this.outfitService.modifyOutfit(id, outfit);
            if (outfit1!=null && this.userService.getUserById(username)!=null){
                return new ResponseEntity<>(this.userService.getUserById(username), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
