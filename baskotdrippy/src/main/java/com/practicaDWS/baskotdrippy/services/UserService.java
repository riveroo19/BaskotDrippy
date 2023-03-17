package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    //map where every user will be saved
    private Map<String, User> users = new ConcurrentHashMap<>();

    //constructors
    public UserService(){
        createUser(new User("Riverrut", "Javier Rivero", "r1ver00t development :D", "12345secure", "riverrut@gmail.com"));
        createUser(new User("Mr.Shark", "Iván Márquez", "biskot babadingshit", "12345secure", "chark@mail.com"));
        createUser(new User("jvalserac", "Javier Valsera", "ª casabillhardt", "12345secure", "quepazatigre@email.com"));
    }

    //CRUD functionalities
    public Collection<User> getUsers (){
        return this.users.values().stream().toList();
    }

    public User getUserById(String username){
        return this.users.get(username); //if doesnt exists, returns null
    }

    public User deleteUser (String username){
        return this.users.remove(username); //if doesnt exists, returns null
    }

    public User createUser (User user){ //values set in controllers
        if (!this.users.containsKey(user.getUsername())){
            this.users.put(user.getUsername(), user);
            return user;
        }
        return null;
    }

    public User modifyUser(String username, User user){ //this user will be created in controllers
        if (this.users.containsKey(username) && user.getUsername().equals(username)){
            user.setCreatedOutfits(this.users.get(username).getCreatedOutfits()); //we will save old user's outfits into this; this method will only change user's details
            user.setUsername(username);
            this.users.put(username, user); //put overrides if exists
            return user;
        }//if it's not registered, it cant be changed, and if it is not the same user, cant do modification
        return null;
    }

    //User's outfits list functionalities (it will add/delete/modify one user's outfit from outfitService)

    public Outfit addOutfit (Outfit outfit){ //will be called by outfitService -> data received in outfit controllers
        String username = outfit.getAuthorUsername();
        if (this.users.containsKey(username)){
           return this.users.get(username).addOutfit(outfit);
        }
        return null;
    }

    public Outfit deleteOutfit (Long idOutfit, String username){ //will be called by outfitService -> data received in outfit controllers
        if (this.users.containsKey(username)){
            return this.users.get(username).deleteOutfit(idOutfit);
        }
        return null;
    }

    public Outfit modifyOutfit(Outfit outfit, String username){ //will be called by outfitService -> data received in outfit controllers
        if (this.users.containsKey(username)){
            return this.users.get(username).modifyOutfit(outfit);
        }
        return null;
    }

    //garments

    public void addGarment(String username, Long outfitId, Garment garment) {
        this.users.get(username).addGarment(outfitId, garment);
    }

    public void deleteGarment(Long garmentId) {
        for (User user : this.users.values()){
            this.users.get(user.getUsername()).deleteGarment(garmentId);
        }
    }

    public void modifyGarment(Long id, Garment garment) {
        for (User user : this.users.values()){
            this.users.get(user.getUsername()).modifyGarment(id, garment);
        }
    }


}
