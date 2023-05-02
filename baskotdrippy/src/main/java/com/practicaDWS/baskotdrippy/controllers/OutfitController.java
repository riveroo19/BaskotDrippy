package com.practicaDWS.baskotdrippy.controllers;


import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.entities.Outfit;
import com.practicaDWS.baskotdrippy.queries.QuerySearchOutfit;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import com.practicaDWS.baskotdrippy.services.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("outfits", this.outfitService.getOutfits());
            return "showOutfits";
        }else {
            Collection<Outfit> outfits = this.outfitService.getOutfits();
            outfits.removeIf(outfit -> outfit.getAuthor().getRoles().contains("ADMIN"));
            model.addAttribute("outfits", outfits);
            return "showOutfits";
        }
    }

    @GetMapping("/outfits/{id}")
    public String getOutfitById(Model model, @PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Outfit outfit = this.outfitService.getOutfitById(id);
        if (outfit==null){
            return "redirect:/error";
        }else {
            boolean isAdmin = auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = auth.getName().equals(outfit.getOwner()) || isAdmin;
            model.addAttribute("outfit", outfit);
            if (isOwner){
                model.addAttribute("garmentsOwner", outfit.getOutfitElements());
            }else {
                model.addAttribute("garments", outfit.getOutfitElements());
            }
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("idOutfit", outfit.getId());
            return "detailOutfit";
        }
    }

    @GetMapping("/outfits/deleteOutfit/{id}")
    public String deleteOutfit(@PathVariable("id")Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Outfit outfit = this.outfitService.getOutfitById(id);
        if (outfit!=null){
            if (auth.getName().equals(outfit.getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                this.outfitService.deleteOutfit(id);
                return "deletionSuccess";
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/deleteOutfits")
    public String deleteOutfits(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            this.outfitService.deleteOutfits();
            return "redirect:/outfits";
        }
        return "redirect:/error";
    }

    @PostMapping("/outfits/createOutfit")
    public String createOutfit(@RequestParam("outfitName") String outfitName, @RequestParam("owner") String owner, @RequestParam("description") String description){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (outfitName.length()!=0 && (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")) || auth.getName().equals(owner))){
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Outfit outfit = this.outfitService.getOutfitById(id);
        if (outfit!=null){
            if ((auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN")) || auth.getName().equals(outfit.getOwner()))){
                model.addAttribute("outfit", outfit);
                return "updateOutfit";
            }
        }
        return "redirect:/error";
    }

    @PostMapping("/outfits/modifySuccess")
    public String modifySuccess(@RequestParam("id") Long id, @RequestParam("outfitName") String outfitName,
                                @RequestParam("owner") String owner, @RequestParam("description") String description){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(owner) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            Outfit outfit = this.outfitService.modifyOutfit(id, new Outfit(outfitName, description, owner));
            if (outfit==null){
                return "redirect:/error";
            }
            String returnvalue = "redirect:/users/" + owner;
            return returnvalue;
        }
        return "redirect:/error";
    }

    @GetMapping("/outfits/{idOutfit}/quitGarment/{id}")
    public String quitGarment(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().equals(this.outfitService.getOutfitById(idOutfit).getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            this.outfitService.quitGarment(id, idOutfit);
            this.garmentService.quitOutfit(this.outfitService.getOutfitById(idOutfit), id);
            String returnvalue = "redirect:/outfits/"+idOutfit;
            return returnvalue;
        }
        return "redirect:/error";
    }

    @GetMapping("/outfits/{idOutfit}/addGarment")
    public String addGarment(Model model, @PathVariable("idOutfit") Long idOutfit){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Outfit outfit = this.outfitService.getOutfitById(idOutfit);
        if (outfit!=null){
            if (auth.getName().equals(outfit.getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                model.addAttribute("garments", this.garmentService.getGarments());
                model.addAttribute("idOutfit", idOutfit);
                return "garmentsToAdd";
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/outfits/{idOutfit}/{id}/success")
    public String addGarmentSuccess(@PathVariable("idOutfit") Long idOutfit, @PathVariable("id") Long id){
        if (this.outfitService.getOutfitById(idOutfit)!=null && this.garmentService.getGarmentById(id)!=null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth.getName().equals(this.outfitService.getOutfitById(idOutfit).getOwner()) || auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                this.outfitService.addGarment(this.garmentService.getGarmentById(id), idOutfit);
                this.garmentService.addOutfit(this.outfitService.getOutfitById(idOutfit), id);
                String returnvalue = "redirect:/outfits/"+idOutfit;
                return returnvalue;
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/searchOutfits")
    public String searchOutfit(Model model, @RequestParam String search){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            model.addAttribute("outfits", this.querySearchOutfit.searchByWord(search));
            return "showOutfits";
        }else {
            Collection<Outfit> outfits = this.querySearchOutfit.searchByWord(search);
            outfits.removeIf(outfit -> outfit.getAuthor().getRoles().contains("ADMIN"));
            model.addAttribute("outfits", outfits);
            return "showOutfits";
        }
    }

}
