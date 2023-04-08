package com.practicaDWS.baskotdrippy.controllers;


import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.queries.QuerySearchOutfit;
import com.practicaDWS.baskotdrippy.services.GarmentService;
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
    @Autowired
    GarmentService garmentService;
    @Autowired
    QuerySearchOutfit querySearchOutfit;

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
        model.addAttribute("garments", outfit.getOutfitElements());
        model.addAttribute("idOutfit", outfit.getId());
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
    public String createOutfit(@RequestParam("outfitName") String outfitName, @RequestParam("owner") String owner, @RequestParam("description") String description){
        if (outfitName.length()!=0){
            Outfit outfit= this.outfitService.createOutfit(new Outfit(outfitName, description, owner));
            if (outfit==null){
                return "redirect:/error";
            }
            String returnvalue = "redirect:/users/" + owner;
            return returnvalue;
        }
        return "redirect:/error";
    }

    @GetMapping("/outfits/updateOutfit/{id}")
    public String updateOutfit(Model model,@PathVariable("id") Long id){
        Outfit outfit = this.outfitService.getOutfitById(id);
        model.addAttribute("outfit", outfit);
        return "updateOutfit";
    }

    @GetMapping("/outfits/modifySuccess")
    public String modifySuccess(@RequestParam("id") Long id, @RequestParam("outfitName") String outfitName,
                                @RequestParam("owner") String owner, @RequestParam("description") String description){
        Outfit outfit = this.outfitService.modifyOutfit(id, new Outfit(outfitName, description, owner));
        if (outfit==null){
            return "redirect:/error";
        }
        String returnvalue = "redirect:/users/" + owner;
        return returnvalue;
    }

    @GetMapping("/outfits/{idOutfit}/quitGarment/{id}")
    public String quitGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        this.outfitService.quitGarment(id, idOutfit);
        this.garmentService.quitOutfit(this.outfitService.getOutfitById(idOutfit), id);
        String returnvalue = "redirect:/outfits/"+idOutfit;
        return returnvalue;
    }

    @GetMapping("/outfits/{idOutfit}/addGarment")
    public String addGarment(Model model, @PathVariable("idOutfit") Long idOutfit){
        model.addAttribute("garments", this.garmentService.getGarments());
        model.addAttribute("idOutfit", idOutfit);
        return "garmentsToAdd";
    }

    @GetMapping("/outfits/{idOutfit}/{id}/success")
    public String addGarmentSuccess(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null) {
            this.outfitService.addGarment(this.garmentService.getGarmentById(id), idOutfit);
            this.garmentService.addOutfit(this.outfitService.getOutfitById(idOutfit), id);
            String returnvalue = "redirect:/outfits/"+idOutfit;
            return returnvalue;
        }
        return "redirect:/error";
    }

    @GetMapping("/searchOutfits")
    public String searchOutfit(Model model, @RequestParam String search){
        model.addAttribute("outfits", this.querySearchOutfit.searchByWord(search));
        return "showOutfits";
    }

}
