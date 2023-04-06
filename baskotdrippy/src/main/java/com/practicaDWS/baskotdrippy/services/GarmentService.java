package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.repositories.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GarmentService {

    //private Map<Long, Garment> garments = new ConcurrentHashMap<>();
    //private AtomicLong lastId = new AtomicLong();

    @Autowired
    private GarmentRepository garmentRepository;

    //@Autowired
    //OutfitService outfitService;
    @Autowired
    //UserService userService;

    //constructors
    public GarmentService(){
        //createGarment(new Garment("airforce1", "nike.com", "zapatillas"));
    }


    //CRUD functionalities
    public Collection<Garment> getGarments(){
        return this.garmentRepository.findAll();
    }

    public Garment getGarmentById(Long id){
        if (this.garmentRepository.existsById(id)){//do this way, if not will throw some errors (exceptions)
            return this.garmentRepository.findById(id).get();
        }
        return null;
    }

    @Transactional
    public Garment deleteGarment (Long id){
        if (this.garmentRepository.existsById(id)){
            //this.outfitService.deleteGarment(id); deprecated...
            //this.userService.deleteGarment(id); deprecated...
            Garment garment = this.garmentRepository.findById(id).get();
            this.garmentRepository.deleteById(id);
            return garment;
        }
        return null;
    }

    public Garment createGarment(Garment garment){
        this.garmentRepository.save(garment);
        return garment;
    }

    @Transactional
    public Garment modifyGarment(Long id, Garment garment){
        if (this.garmentRepository.existsById(id)){
            garment.setId(id); //just in case
            //this.outfitService.modifyGarment(id, garment); deprecated...
            //this.userService.modifyGarment(id, garment);
            this.garmentRepository.save(garment);
            return garment;
        }
        return null;
    }


}
