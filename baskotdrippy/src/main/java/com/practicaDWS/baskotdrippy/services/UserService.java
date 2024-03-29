package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.repositories.UserRepository;
import org.owasp.html.Sanitizers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    //map where every user will be saved
    //private Map<String, User> users = new ConcurrentHashMap<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //constructors


    //CRUD functionalities
    public Collection<User> getUsers (){
        return this.userRepository.findAll();
    }


    public User getUserById(String username){
        if (this.userRepository.existsById(username)){
            return this.userRepository.findById(username).get(); //if doesnt exists, returns null, but to avoid errors we check key before
        }
        return null;
    }

    @Transactional
    public User deleteUser (String username){
        if (this.userRepository.existsById(username)){
            User user = this.userRepository.findById(username).get();
            this.userRepository.deleteById(username); //cascade.all will drop this user's outfits
            return user;
        }
        return null;
    }
    @Transactional
    public void deleteUsers(){
        this.userRepository.deleteAllByRolesNotContaining("ADMIN");
    }

    public User createUser (User user){ //values set in controllers
        if (!this.userRepository.existsById(user.getUsername())){
            user.setUsername(Sanitizers.FORMATTING.sanitize(user.getUsername()));
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            user.setRoles(roles);
            this.userRepository.save(user);
            return user;
        }
        return null;
    }

    @Transactional
    public User modifyUser(String username, User user){ //this user will be created in controllers
        if (this.userRepository.existsById(username) && user.getUsername().equals(username)){
            user.setUsername(username);
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            List<String> roles = new ArrayList<>();
            roles.add("USER");
            user.setRoles(roles);
            this.userRepository.save(user); //will override if exists
            return user;
        }//if it's not registered, it cant be changed, and if it is not the same user, cant do modification
        return null;
    }

    //User's outfits list functionalities (it will add/delete/modify one user's outfit from outfitService)
    //bbdd override this and doesn't need this functionality, nevertheless we save it here just in case

    /*public Outfit addOutfit (Outfit outfit){ //will be called by outfitService -> data received in outfit controllers
        //deprecated since database implementations...
        String username = outfit.getOwner();
        if (this.userRepository.existsById(username)){
           return this.userRepository.findById(username).get().addOutfit(outfit);
        }
        return null;
    }*/

    public Outfit deleteOutfit (Long idOutfit, String username){ //will be called by outfitService -> data received in outfit controllers
        if (this.userRepository.existsById(username)){
            return this.userRepository.findById(username).get().deleteOutfit(idOutfit);
        }
        return null;
    }

    /*public Outfit modifyOutfit(Outfit outfit, String username){ //will be called by outfitService -> data received in outfit controllers
        //deprecated since bbdd
        if (this.userRepository.existsById(username)){
            return this.userRepository.findById(username).get().modifyOutfit(outfit);
        }
        return null;
    }*/

    //garments

    /*public void deleteGarment(Long garmentId) { //deprecated...
        for (User user : this.userRepository.findAll()){
            this.userRepository.findById(user.getUsername()).get().deleteGarment(garmentId);
        }
    }*/

    /*public void modifyGarment(Long id, Garment garment) {//deprecated
        for (User user : this.userRepository.findAll()){
            this.userRepository.findById(user.getUsername()).get().modifyGarment(id, garment);
        }
    }*/


}
