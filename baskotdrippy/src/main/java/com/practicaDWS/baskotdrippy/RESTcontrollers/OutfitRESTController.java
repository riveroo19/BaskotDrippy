package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;



@RestController
@RequestMapping("/api")
public class OutfitRESTController {

    @Autowired
    OutfitService outfitService;
    @Autowired
    GarmentService garmentService;

    //CRUD functionalities
    @GetMapping("/outfits")
    public ResponseEntity<Collection<Outfit>> getOutfits(){
        return new ResponseEntity<>(this.outfitService.getOutfits(), HttpStatus.OK);
    }

    @GetMapping("/outfits/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        if (outfit!=null){
            return new ResponseEntity<>(outfit, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/outfits/{id}")
    public ResponseEntity<Outfit> deleteOutfit(@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.deleteOutfit(id);
        if (outfit!=null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/outfits/{id}")
    public ResponseEntity<Outfit> modifyOutfit(@PathVariable("id") Long id, @RequestBody Outfit outfit){
        Outfit outfit1 = this.outfitService.modifyOutfit(id, outfit);
        if (outfit1!=null){
            return new ResponseEntity<>(outfit1, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    @PostMapping("/outfits")
    public ResponseEntity<Outfit> createOutfit(@RequestBody Outfit outfit){
        Outfit outfit1 = this.outfitService.createOutfit(outfit);
        if (outfit1!=null){
            return new ResponseEntity<>(outfit1, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //the specified owner is not registered, so bad request
    }

    //others

    @PatchMapping("/outfits/{idOutfit}/addGarment/{id}")//by id
    public ResponseEntity<Outfit> addGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null){
            this.outfitService.addGarment(this.garmentService.getGarmentById(id), idOutfit);
            this.garmentService.addOutfit(this.outfitService.getOutfitById(idOutfit), id);
            return new ResponseEntity<>(this.outfitService.getOutfitById(idOutfit),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/outfits/{idOutfit}/quitGarment/{id}")
    public ResponseEntity<Outfit> quitGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null){
            this.outfitService.quitGarment(id, idOutfit);
            this.garmentService.quitOutfit(this.outfitService.getOutfitById(idOutfit), id);
            return new ResponseEntity<>(outfitService.getOutfitById(idOutfit), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
