package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GarmentService {

    private Map<Long, Garment> garments = new ConcurrentHashMap<>();
    private AtomicLong lastId = new AtomicLong();

    @Autowired
    OutfitService outfitService;
    @Autowired
    UserService userService;

    //constructors
    public GarmentService(){
        createGarment(new Garment("airforce1", "nike.com", "zapatillas"));
    }


    //CRUD functionalities
    public Collection<Garment> getGarments(){
        return this.garments.values().stream().toList();
    }

    public Garment getGarmentById(Long id){
        return this.garments.get(id);
    }

    public Garment deleteGarment (Long id){
        if (this.garments.get(id)!=null){
            this.outfitService.deleteGarment(id);
            this.userService.deleteGarment(id);
            return this.garments.remove(id);
        }
        return null;
    }

    public Garment createGarment(Garment garment){
        garment.setId(lastId.incrementAndGet());
        this.garments.put(lastId.get(), garment);
        return garment;
    }

    public Garment modifyGarment(Long id, Garment garment){
        if (this.garments.containsKey(id)){
            garment.setId(id);
            this.outfitService.modifyGarment(id, garment);
            this.userService.modifyGarment(id, garment);
            this.garments.put(id, garment);
            return garment;
        }
        return null;
    }


}
