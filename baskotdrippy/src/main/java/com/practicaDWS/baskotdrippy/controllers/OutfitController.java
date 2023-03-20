package com.practicaDWS.baskotdrippy.controllers;


import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OutfitController {

    @Autowired
    OutfitService outfitService;

    @GetMapping("/outfits")
    public String getOutfits(Model model){
        model.addAttribute("outfits", this.outfitService.getOutfits());
        return "showOutfits";
    }

    @GetMapping("/outfits/{id}")
    public String getOutfitById(Model model, @PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        if (outfit==null){
            return "redirect:/error";
        }
        model.addAttribute("outfit", outfit);
        model.addAttribute("garments", outfit.getOutfitElements().values());
        return "detailOutfit";
    }

    @GetMapping("/outfits/deleteOutfit/{id}")
    public String deleteOutfit(@PathVariable("id")Long id){
        Outfit outfit = this.outfitService.deleteOutfit(id);
        if(outfit==null){
            return "redirect:/error";
        }
        return "deletionSuccess";
    }

    @PostMapping("/outfits/createOutfit")
    public String createOutfit(@RequestParam("outfitName") String outfitName, @RequestParam("authorUsername") String authorUsername, @RequestParam("description") String description){
        Outfit outfit= this.outfitService.createOutfit(new Outfit(outfitName, description, authorUsername));
        if (outfit==null){
            return "redirect:/error";
        }
        String returnvalue = "redirect:/users/" + authorUsername;
        return returnvalue;
    }

    @GetMapping("/outfits/updateOutfit/{id}")
    public String updateOutfit(Model model,@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        model.addAttribute("outfit", outfit);
        return "updateOutfit";
    }

    @GetMapping("/outfits/modifySuccess")
    public String modifySuccess(@RequestParam("id") Long id, @RequestParam("outfitName") String outfitName,
                                @RequestParam("authorUsername") String authorUsername, @RequestParam("description") String description){
        Outfit outfit = this.outfitService.modifyOutfit(id, new Outfit(outfitName, description, authorUsername));
        if (outfit==null){
            return "redirect:/error";
        }
        String returnvalue = "redirect:/users/" + authorUsername;
        return returnvalue;
    }

}
