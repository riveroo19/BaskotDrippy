package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.fasterxml.jackson.annotation.JsonView;
import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.services.GarmentService;
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
public class GarmentRESTController {

    @Autowired
    GarmentService garmentService;

    @JsonView(Garment.GarmentView.class)
    @GetMapping("/garments")
    public ResponseEntity<Collection<Garment>> getGarments(){
        return new ResponseEntity<>(this.garmentService.getGarments(), HttpStatus.OK);
    }

    @JsonView(Garment.GarmentView.class)
    @GetMapping("/garments/{id}")
    public ResponseEntity<Garment> getGarmentById(@PathVariable("id") Long id){
        Garment garment = this.garmentService.getGarmentById(id);
        if (garment!=null){
            return new ResponseEntity<>(garment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @JsonView(Garment.GarmentView.class)
    @DeleteMapping("/garments/{id}")
    public ResponseEntity<Garment> deleteGarment(@PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSessionAdmin(auth)){
            Garment garment = this.garmentService.deleteGarment(id);
            if (garment!=null){
                return new ResponseEntity<>(garment, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(Garment.GarmentView.class)
    @PutMapping("/garments/{id}")
    public ResponseEntity<Garment> modifyGarment (@PathVariable("id") Long id, @RequestBody Garment garment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSessionAdmin(auth)){
            Garment garment1 = this.garmentService.modifyGarment(id, garment);
            if (garment1!=null){
                return new ResponseEntity<>(garment, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @JsonView(Garment.GarmentView.class)
    @PostMapping("/garments")
    public ResponseEntity<Garment> createGarment(@RequestBody Garment garment){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSessionAdmin(auth) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_USER"))){
            return new ResponseEntity<>(this.garmentService.createGarment(garment), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/deleteGarments")
    public ResponseEntity<Collection<Garment>> deleteGarments(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (checkSessionAdmin(auth)){
            this.garmentService.deleteGarments();
            Collection<Garment> garments = this.garmentService.getGarments();
            return new ResponseEntity<>(garments, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    private boolean checkSessionAdmin(Authentication auth){
        return auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken) &&
                auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
    }
}
