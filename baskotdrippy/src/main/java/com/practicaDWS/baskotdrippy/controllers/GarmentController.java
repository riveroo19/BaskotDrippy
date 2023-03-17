package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GarmentController {

    @Autowired
    GarmentService garmentService;

    @GetMapping("/garments")
    public String getUsers(Model model){
        model.addAttribute("garments", this.garmentService.getGarments());
        return "showGarments";
    }

    @GetMapping("/garments/{id}")
    public String getGarmentById(Model model, @PathVariable("id") Long id){
        Garment garment = this.garmentService.getGarmentById(id);
        if (garment==null){
            return "redirect:error";
        }
        model.addAttribute("garment", garment);
        return "showGarment";
    }

    @DeleteMapping("/garments/{id}")
    public String deleteGarmentById(@PathVariable("id") Long id){
        Garment garment = this.garmentService.deleteGarment(id);
        if (garment==null){
            return "redirect:error";
        }
        return "deletionSuccess";
    }

}
