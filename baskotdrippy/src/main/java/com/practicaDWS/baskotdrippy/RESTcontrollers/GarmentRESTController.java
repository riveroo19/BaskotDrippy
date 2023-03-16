package com.practicaDWS.baskotdrippy.RESTcontrollers;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api")
public class GarmentRESTController {

    @Autowired
    GarmentService garmentService;

    @GetMapping("/garments")
    public ResponseEntity<Collection<Garment>> getGarments(){
        return new ResponseEntity<>(this.garmentService.getGarments(), HttpStatus.OK);
    }

    @GetMapping("/garments/{id}")
    public ResponseEntity<Garment> getGarmentById(@PathVariable("id") Long id){
        Garment garment = this.garmentService.getGarmentById(id);
        if (garment!=null){
            return new ResponseEntity<>(garment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/garments/{id}")
    public ResponseEntity<Garment> deleteGarment(@PathVariable("id") Long id){
        Garment garment = this.garmentService.deleteGarment(id);
        if (garment!=null){
            return new ResponseEntity<>(garment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/garments/{id}")
    public ResponseEntity<Garment> modifyGarment (@PathVariable("id") Long id, @RequestBody Garment garment){
        Garment garment1 = this.garmentService.modifyGarment(id, garment);
        if (garment1!=null){
            return new ResponseEntity<>(garment, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/garments")
    public ResponseEntity<Garment> createGarment(@RequestBody Garment garment){
        return new ResponseEntity<>(this.garmentService.createGarment(garment), HttpStatus.OK);
    }

}
