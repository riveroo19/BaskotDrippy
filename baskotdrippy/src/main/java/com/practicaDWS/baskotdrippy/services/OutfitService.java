package com.practicaDWS.baskotdrippy.services;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OutfitService {

    private Map<Long, Outfit> outfits = new ConcurrentHashMap<>();
    private AtomicLong lastId = new AtomicLong();

    @Autowired
    UserService userService;

    //constructors
    public OutfitService(){

    }

    //CRUD functionalities
    public Collection<Outfit> getOutfits (){
        //if we want to make cascade deletion, we have to check outfit by outfit if the owner still exists, and if not, that means that user has been deleted, so its outfits too
        for (Outfit outfit : outfits.values()){
            if (!this.existUser(outfit.getAuthorUsername())){
                this.outfits.remove(outfit.getId());
            }
        }
        return this.outfits.values().stream().toList();
    }

    public Outfit getOutfitById (Long id){
        return this.outfits.get(id);
    }

    public Outfit deleteOutfit(Long id){
        Outfit outfit = this.outfits.get(id);
        if (outfit!=null){//must delete this outfit from his user's outfits list
            if (existUser(outfit.getAuthorUsername())){
                this.userService.deleteOutfit(outfit, outfit.getAuthorUsername());
            }
            this.outfits.remove(id);
            return outfit;
        }
        return null;
    }

    public Outfit createOutfit(Outfit outfit){ //values set in controllers
        if (existUser(outfit.getAuthorUsername())){
            outfit.setId(this.lastId.incrementAndGet());
            this.outfits.put(this.lastId.get(), outfit);
            userService.addOutfit(outfit);
            return outfit;
        }
        return null;
    }

    public Outfit modifyOutfit(Long id, Outfit outfit){ //values set in controllers
        if (this.outfits.get(id)!=null && existUser(outfit.getAuthorUsername())){ //if the user doesn't exist (or bad introduced), won't change it
            outfit.setOutfitElements(this.outfits.get(id).getOutfitElements()); //set old outfit elements bc this method will only change
            outfit.setId(id);
            this.userService.modifyOutfit(outfit, outfit.getAuthorUsername());
            this.outfits.put(id, outfit);
            return outfit;
        }
        return null;
    }

    //garments functionalities (it will add/delete/modify one garment in any outfit that wears it)

    public void addGarment (Garment garment, Long outfitId){ //values set in controllers: the controller will collect the garment by its id and construct it
        if (this.outfits.containsKey(outfitId)){
            this.outfits.get(outfitId).addGarment(garment);
            String username = this.outfits.get(outfitId).getAuthorUsername();
            this.userService.addGarment(username, outfitId, garment);
        };
    }

    public void quitGarment (Long garmentId, Long outfitId){
        if (this.outfits.containsKey(outfitId)){
            this.outfits.get(outfitId).deleteGarment(garmentId);
        }
    }


    public void deleteGarment (Long garmentId){
        for (Outfit outfit : outfits.values()){
            if (!outfit.getOutfitElements().isEmpty()){
                outfits.get(outfit.getId()).deleteGarment(garmentId);
            }
        }
    }

    public void modifyGarment(Long id, Garment garment) {
        for (Outfit outfit : outfits.values()){
            if (!outfit.getOutfitElements().isEmpty()){
                outfits.get(outfit.getId()).modifyGarment(id, garment);
            }
        }
    }

    //extra utilities
    public boolean existUser(String username){
        return this.userService.getUserById(username) != null; //if it's not null, TRUE, if it is, FALSE
    }


}
