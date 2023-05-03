package com.practicaDWS.baskotdrippy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Garment {

    public interface GarmentView{}

    @JsonView({GarmentView.class, Outfit.OutfitView.class, User.UserView.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id

    @JsonView({GarmentView.class, Outfit.OutfitView.class, User.UserView.class})
    private String garmentName;
    @JsonView({GarmentView.class, Outfit.OutfitView.class, User.UserView.class})
    private String url;
    @JsonView({GarmentView.class, Outfit.OutfitView.class, User.UserView.class})
    private String type; //maybe glasses, t-shirt...

    @JsonIgnore
    @ManyToMany(mappedBy = "outfitElements", cascade = CascadeType.REFRESH)
    private List<Outfit> outfits = new ArrayList<>();


    public Garment (String garmentName, String url, String type){
        this.garmentName = garmentName;
        this.url = url;
        this.type = type;
    }

    @Transactional
    public void addOutfit(Outfit outfit){
        if (!this.outfits.contains(outfit)){
            this.outfits.add(outfit);
        }
    }

    @Transactional
    public void quitOutfit(Outfit outfit){
        this.outfits.remove(outfit);
    }


}
