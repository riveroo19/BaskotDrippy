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
public class Garment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id

    private String garmentName;
    private String url;
    private String type; //maybe glasses, t-shirt...

    @JsonIgnore
    @ManyToMany(mappedBy = "outfitElements", cascade = CascadeType.ALL)
    private List<Outfit> outfits = new ArrayList<>();

    @PreRemove
    private void delete(){
        this.outfits.clear();
    }

    public Garment (String garmentName, String url, String type){
        this.garmentName = garmentName;
        this.url = url;
        this.type = type;
    }


}
