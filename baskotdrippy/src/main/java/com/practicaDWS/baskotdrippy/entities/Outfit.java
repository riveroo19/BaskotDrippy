package com.practicaDWS.baskotdrippy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Outfit {

    private Long id; //id
    private String outfitName;
    private String description;
    private String authorUsername;

    //@JsonIgnore
    //private User author;

    //future related table between garments and outfits
    private Map<Long, Garment> outfitElements = new HashMap<>();

    public Outfit (String outfitName, String description, String authorUsername){
        this.outfitName = outfitName;
        this.description = description;
        this.authorUsername = authorUsername;
    }

    public void addGarment(Garment garment) {
        if (!this.outfitElements.containsKey(garment.getId())){
            this.outfitElements.put(garment.getId(), garment);
        }
    }

    public Garment deleteGarment(Long garmentId) {
        if (this.outfitElements.containsKey(garmentId)){
            return this.outfitElements.remove(garmentId);
        }
        return null;
    }


    public void modifyGarment(Long id, Garment garment) {
        if (this.outfitElements.containsKey(id)){
            this.outfitElements.put(id, garment);
        }
    }
}
