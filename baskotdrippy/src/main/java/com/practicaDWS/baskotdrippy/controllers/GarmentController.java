package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "detailGarment";
    }

    @GetMapping("/garments/deleteGarment/{id}")
    public String deleteGarmentById(@PathVariable("id") Long id){
        Garment garment = this.garmentService.deleteGarment(id);
        if (garment==null){
            return "redirect:/error";
        }
        return "deletionSuccess";
    }

    @GetMapping("/garments/createGarment")
    public String createGarment(@RequestParam("garmentName") String garmentName, @RequestParam("type") String type, @RequestParam("url") String url){
        Garment garment = this.garmentService.createGarment(new Garment(garmentName, url, type));
        return "redirect:/garments";
    }

    @GetMapping("/garments/updateGarment/{id}")
    public String modifyGarment(Model model, @PathVariable("id") Long id){
        Garment garment = this.garmentService.getGarmentById(id);
        model.addAttribute("garment", garment);
        return "updateGarment";
    }

    @GetMapping("/garments/modifySuccess")
    public String modifySuccess(Model model, @RequestParam("id") Long id, @RequestParam("garmentName") String garmentName, @RequestParam("type") String type, @RequestParam("url") String url){
        Garment garment = this.garmentService.modifyGarment(id, new Garment(garmentName, url, type));
        if (garment==null){
            return "redirect:/error";
        }
        return this.getGarmentById(model, id);
    }

}
