package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.practicaDWS.baskotdrippy.entities.User;
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
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{username}")
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
}
