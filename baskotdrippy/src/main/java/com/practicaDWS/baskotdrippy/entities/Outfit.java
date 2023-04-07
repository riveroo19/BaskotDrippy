package com.practicaDWS.baskotdrippy.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class Outfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id

    private String outfitName;
    private String description;
    private String owner;

    @JsonIgnore
    @ManyToOne
    private User author;

    @JoinTable(name = "outfit_garment",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "garment_id"))
    @ManyToMany(cascade = CascadeType.REFRESH)
    private List<Garment> outfitElements = new ArrayList<>();


    public Outfit(String outfitName, String description, String owner){
        this.outfitName = outfitName;
        this.description = description;
        this.owner = owner;
    }

    public Outfit(String outfitName, String description, User author){
        this.outfitName = outfitName;
        this.description = description;
        this.owner = author.getUsername();
        this.author = author;
    }

    public Outfit (String outfitName, String description){
        this.outfitName = outfitName;
        this.description = description;
    }

    @Transactional
    public void addGarment(Garment garment) {
        if (!this.outfitElements.contains(garment)){
            this.outfitElements.add(garment);
        }
    }

    @Transactional
    public Garment deleteGarment(Long garmentId) {
        for (Garment garment : outfitElements){
            if (garmentId.equals(garment.getId())){
                this.outfitElements.remove(garment);
                return garment;
            }
        }
        return null;
    }


    /*public void modifyGarment(Long id, Garment garment) {//deprecated
        for (int i = 0; i < this.outfitElements.size(); i++) {
            if (id.equals(this.outfitElements.get(i).getId())){
                this.outfitElements.set(i, garment);
            }
        }
    }*/
}
