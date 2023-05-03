package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;



@RestController
@RequestMapping("/api")
public class OutfitRESTController {

    @Autowired
    OutfitService outfitService;
    @Autowired
    GarmentService garmentService;

    @JsonView(Outfit.OutfitView.class)
    //CRUD functionalities
    @GetMapping("/outfits")
    public ResponseEntity<Collection<Outfit>> getOutfits(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            return new ResponseEntity<>(this.outfitService.getOutfits(), HttpStatus.OK);
        }
        Collection<Outfit> outfits = this.outfitService.getOutfits();
        outfits.removeIf(outfit -> outfit.getAuthor().getRoles().contains("ADMIN"));
        return new ResponseEntity<>(outfits, HttpStatus.OK);
    }

    @JsonView(Outfit.OutfitView.class)
    @GetMapping("/outfits/{id}")
    public ResponseEntity<Outfit> getOutfitById(@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (outfit!=null){
            if (outfit.getAuthor().getRoles().contains("ADMIN") && auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(outfit, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Outfit.OutfitView.class)
    @DeleteMapping("/outfits/{id}")
    public ResponseEntity<Outfit> deleteOutfit(@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (outfit!=null){
            if (checkSession(auth) && (auth.getName().equals(outfit.getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
                this.outfitService.deleteOutfit(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Outfit.OutfitView.class)
    @PatchMapping("/outfits/{id}")
    public ResponseEntity<Outfit> modifyOutfit(@PathVariable("id") Long id, @RequestBody Outfit outfit){
        Outfit outfit1 = this.outfitService.getOutfitById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (outfit1!=null){
            if (checkSession(auth) && (auth.getName().equals(outfit1.getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
                Outfit outfit2 = this.outfitService.modifyOutfit(id, outfit);
                if (outfit2!=null){
                    return new ResponseEntity<>(outfit1, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Outfit.OutfitView.class)
    @PostMapping("/outfits")
    public ResponseEntity<Outfit> createOutfit(@RequestBody Outfit outfit){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && (auth.getName().equals(outfit.getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
            Outfit outfit1 = this.outfitService.createOutfit(outfit);
            if (outfit1!=null){
                return new ResponseEntity<>(outfit1, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //the specified owner is not registered, so bad request
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //others

    @JsonView(Outfit.OutfitView.class)
    @PatchMapping("/outfits/{idOutfit}/addGarment/{id}")//by id
    public ResponseEntity<Outfit> addGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (checkSession(auth) && (auth.getName().equals(this.outfitService.getOutfitById(idOutfit).getOwner()) ||
                    auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
                this.outfitService.addGarment(this.garmentService.getGarmentById(id), idOutfit);
                this.garmentService.addOutfit(this.outfitService.getOutfitById(idOutfit), id);
                return new ResponseEntity<>(this.outfitService.getOutfitById(idOutfit),HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Outfit.OutfitView.class)
    @PatchMapping("/outfits/{idOutfit}/quitGarment/{id}")
    public ResponseEntity<Outfit> quitGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (checkSession(auth) && (auth.getName().equals(this.outfitService.getOutfitById(idOutfit).getOwner()) ||
                    auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")))){
                this.outfitService.quitGarment(id, idOutfit);
                this.garmentService.quitOutfit(this.outfitService.getOutfitById(idOutfit), id);
                return new ResponseEntity<>(outfitService.getOutfitById(idOutfit), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Outfit.OutfitView.class)
    @DeleteMapping("/deleteOutfits")
    public ResponseEntity<Collection<Outfit>> deleteOutfits(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSession(auth) && auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            this.outfitService.deleteOutfits();
            Collection<Outfit> outfits = this.outfitService.getOutfits();
            return new ResponseEntity<>(outfits, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private boolean checkSession(Authentication auth){
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }
}
