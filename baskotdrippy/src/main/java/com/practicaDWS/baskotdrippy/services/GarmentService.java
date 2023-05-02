package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.repositories.GarmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GarmentService {

    //private Map<Long, Garment> garments = new ConcurrentHashMap<>();
    //private AtomicLong lastId = new AtomicLong();

    @Autowired
    private GarmentRepository garmentRepository;

    @Autowired
    OutfitService outfitService;
    @Autowired
    //UserService userService;

    //constructors



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
            this.outfitService.deleteGarment(id);
            //this.userService.deleteGarment(id); deprecated...
            Garment garment = this.garmentRepository.findById(id).get();
            this.garmentRepository.deleteById(id);
            return garment;
        }
        return null;
    }
    @Transactional
    public void deleteGarments() {
        List<Garment> garments = this.garmentRepository.findAll();
        for (Garment garment : garments) {
            this.outfitService.deleteGarment(garment.getId());
            this.garmentRepository.delete(garment);
        }
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


    public void addOutfit(Outfit outfit, Long id) {
        if (this.garmentRepository.existsById(id)){
            this.garmentRepository.findById(id).get().addOutfit(outfit);
            this.garmentRepository.save(this.garmentRepository.findById(id).get());
        }
    }
    @Transactional
    public void quitOutfit(Outfit outfit, Long id) {
        if (this.garmentRepository.existsById(id)){
            this.garmentRepository.findById(id).get().quitOutfit(outfit);
            this.garmentRepository.save(this.garmentRepository.findById(id).get());
        }
    }
}
