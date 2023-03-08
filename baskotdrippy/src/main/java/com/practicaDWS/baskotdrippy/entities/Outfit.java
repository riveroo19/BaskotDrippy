package com.practicaDWS.baskotdrippy.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Outfit {

    private Long id; //id
    private String outfitName;
    private String description;
    //when batadase is set up may change to user_id
    private User author;

    private List<Garment> outfitElements = new ArrayList<>();


}
