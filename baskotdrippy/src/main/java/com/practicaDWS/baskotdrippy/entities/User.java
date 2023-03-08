package com.practicaDWS.baskotdrippy.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {

    private String username; //id
    private String fullname;
    private String bio;
    private String password;
    private String email;

    //maybe wanna add likedOutfits later
    //private List<Outfit> likedOutfits = new ArrayList<>();

    private List<Outfit> createdOutfits = new ArrayList<>();

    public void addOutfit(Outfit outfit){
        this.createdOutfits.add(outfit);
    }


}
