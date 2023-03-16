package com.practicaDWS.baskotdrippy.entities;

import lombok.Data;

@Data
public class Garment {

    private Long id; //id
    private String garmentName;
    private String url;
    private String type; //maybe glasses, t-shirt...

    //private String author;

    public Garment (String garmentName, String url, String type){
        this.garmentName = garmentName;
        this.url = url;
        this.type = type;
    }


}
