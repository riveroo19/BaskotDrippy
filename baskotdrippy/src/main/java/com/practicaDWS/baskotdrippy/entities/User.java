package com.practicaDWS.baskotdrippy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username; //id

    private String fullname;
    private String bio;
    private String password;
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Outfit> createdOutfits = new ArrayList<>();

    public User(String username, String fullname, String bio, String password, String email){
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.fullname = fullname;
        this.email = email;
    }



    public Outfit addOutfit(Outfit outfit){
        for (Outfit outfit1 : createdOutfits){
            if (outfit.getId().equals(outfit1.getId())){
                return null; //if its there, we will not add it
            }
        }
        this.createdOutfits.add(outfit);
        return outfit;
    }

    public Outfit deleteOutfit(Long idOutfit){//good practice, delete at application level
        //if null, return null, if not return the removed outfit
        for (Outfit outfit: createdOutfits){
            if (idOutfit.equals(outfit.getId())){
                this.createdOutfits.remove(outfit);
                return outfit;
            }
        }
        return null;
    }

    public Outfit modifyOutfit(Outfit outfit) { //if its inside will return the new outfit, if not will return null
        for (int i = 0; i < this.createdOutfits.size(); i++) {
            if (outfit.getId().equals(createdOutfits.get(i).getId())){
                this.createdOutfits.set(i, outfit);
                return outfit; //if its there, we will change it
            }
        }
        return null;
    }

    public void addGarment(Long outfitId, Garment garment) {
        for (Outfit outfit : this.createdOutfits) {
            if (outfit.getId().equals(outfitId)) {
                outfit.addGarment(garment);
                return;
            }
        }
    }

    /*public void deleteGarment(Long id) { //deprecated
        for (Outfit outfit : this.createdOutfits) {
            outfit.deleteGarment(id);
        }
    }*/


    /*public void modifyGarment(Long id, Garment garment) {//deprecated
        for (Outfit outfit: this.createdOutfits){
            outfit.modifyGarment(id, garment);
        }
    }*/

    public boolean existOutfit(Long id){
        for (Outfit outfit: createdOutfits){
            if (outfit.getId().equals(id)){
                return true;
            }
        }
        return false;
    }


}
