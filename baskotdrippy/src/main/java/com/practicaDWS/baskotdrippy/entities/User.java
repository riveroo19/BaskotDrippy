package com.practicaDWS.baskotdrippy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class User {

    private String username; //id
    private String fullname;
    private String bio;
    private String password;
    private String email;


    private Map<Long, Outfit> createdOutfits = new HashMap<>();

    public User (String username, String fullname, String bio, String password, String email){
        this.username = username;
        this.fullname = fullname;
        this.bio = bio;
        this.password = password;
        this.email = email;
    }

    public Outfit addOutfit(Outfit outfit){
        if (this.createdOutfits.put(outfit.getId(), outfit)!=null){
            return outfit;
        }
        return null;
    }

    public Outfit deleteOutfit(Long idOutfit){
        return this.createdOutfits.remove(idOutfit); //if null, return null, if not return the removed outfit
    }

    public Outfit modifyOutfit(Outfit outfit) { //if its inside will return the new outfit, if not will return null
        if (this.createdOutfits.get(outfit.getId()) != null){
            createdOutfits.put(outfit.getId(), outfit);
            return outfit;
        }
        return null;
    }

    public void addGarment(Long outfitId, Garment garment) {
        this.createdOutfits.get(outfitId).addGarment(garment);
    }

    public void deleteGarment(Long id) {
        for (Outfit outfit : this.createdOutfits.values()){
            this.createdOutfits.get(outfit.getId()).deleteGarment(id);
        }
    }


    public void modifyGarment(Long id, Garment garment) {
        for (Outfit outfit : this.createdOutfits.values()){
            this.createdOutfits.get(outfit.getId()).modifyGarment(id, garment);
        }
    }


}
