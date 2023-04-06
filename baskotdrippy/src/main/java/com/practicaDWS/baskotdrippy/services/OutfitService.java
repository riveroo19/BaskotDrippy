package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.entities.User;
import com.practicaDWS.baskotdrippy.repositories.OutfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OutfitService {

    //private Map<Long, Outfit> outfits = new ConcurrentHashMap<>();
    //private AtomicLong lastId = new AtomicLong();
    @Autowired
    private OutfitRepository outfitRepository;

    @Autowired
    UserService userService;

    //constructors
    public OutfitService(){

    }

    //CRUD functionalities
    public Collection<Outfit> getOutfits (){
        return this.outfitRepository.findAll();
    }

    public Outfit getOutfitById (Long id){
        if (this.outfitRepository.existsById(id)){//errors if we dont check this
            return this.outfitRepository.findById(id).get();
        }
        return null;
    }

    @Transactional
    public Outfit deleteOutfit(Long id){
        if (this.outfitRepository.existsById(id)){
            Outfit outfit = this.outfitRepository.findById(id).get();
            this.outfitRepository.deleteById(id); //cascade will delete those rows where this outfit appear
            //this.userService.deleteOutfit(id, outfit.getOwner()); deprecated...
            return outfit;
        }
        return null;
    }

    public Outfit createOutfit(Outfit outfit){ //values set in controllers
        if (existUser(outfit.getOwner())){
            outfit.setAuthor(userService.getUserById(outfit.getOwner())); //controllers set
            this.outfitRepository.save(outfit);
            //this.userService.addOutfit(outfit); deprecated...
            return outfit;
        }
        return null;
    }

    @Transactional
    public Outfit modifyOutfit(Long id, Outfit outfit){ //values set in controllers
        if (this.outfitRepository.existsById(id) && existUser(outfit.getOwner()) && this.outfitRepository.findById(id).get().getOwner().equals(outfit.getOwner())){ //if the user doesn't exist (or bad introduced), won't change it
            outfit.setId(id); //just in case
            outfit.setAuthor(this.outfitRepository.findById(id).get().getAuthor()); //string owner is setted by default in controllers (specified in postman/forms)
            this.outfitRepository.save(outfit); //override
            //this.userService.modifyOutfit(outfit, outfit.getOwner()); deprecated...
            return outfit;
        }
        return null;
    }

    //garments functionalities (it will add/delete/modify one garment in any outfit that wears it)


    public void quitGarment (Long garmentId, Long outfitId){
        if (this.outfitRepository.existsById(outfitId)){
            this.outfitRepository.findById(outfitId).get().deleteGarment(garmentId);
        }
    }


    /*public void deleteGarment (Long garmentId){//deprecated
        for (Outfit outfit : outfitRepository.findAll()){
            if (!outfit.getOutfitElements().isEmpty()){
                outfitRepository.findById(outfit.getId()).get().deleteGarment(garmentId);
            }
        }
    }*/

    /*public void modifyGarment(Long id, Garment garment) {//deprecated
        for (Outfit outfit : outfitRepository.findAll()){
            if (!outfit.getOutfitElements().isEmpty()){
                outfitRepository.findById(id).get().modifyGarment(id, garment);
            }
        }
    }*/

    //extra utilities
    public boolean existUser(String username){
        return this.userService.getUserById(username) != null; //if it's not null, TRUE, if it is, FALSE
    }


}
