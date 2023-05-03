package com.practicaDWS.baskotdrippy.controllers;

import com.practicaDWS.baskotdrippy.entities.Garment;
import com.practicaDWS.baskotdrippy.queries.QuerySearchGarment;
import com.practicaDWS.baskotdrippy.services.GarmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class GarmentController {

    @Autowired
    GarmentService garmentService;
    @Autowired
    QuerySearchGarment querySearchGarment;

    /*@PostConstruct uncomment if first time
    public void init(){
        garmentService.createGarment(new Garment("airforce1", "nike.com", "zapatillas"));
        garmentService.createGarment(new Garment("airforce2", "nike.com", "zapatillas"));
        garmentService.createGarment(new Garment("chandal op", "bershka.es", "pantalones"));
    }*/

    @GetMapping("/garments")
    public String getUsers(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogged = auth!=null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("garments", this.garmentService.getGarments());
        model.addAttribute("isLogged", isLogged);
        return "showGarments";
    }

    @GetMapping("/garments/{id}")
    public String getGarmentById(Model model, @PathVariable("id") Long id){
        Garment garment = this.garmentService.getGarmentById(id);
        if (garment==null){
            return "redirect:/error";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("garment", garment);
        model.addAttribute("isAdmin", isAdmin);
        return "detailGarment";
    }

    @GetMapping("/garments/deleteGarment/{id}")
    public String deleteGarmentById(@PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Garment garment = this.garmentService.getGarmentById(id);
        if (garment!=null){
            if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
                this.garmentService.deleteGarment(id);
                return "deletionSuccess";
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/deleteGarments")
    public String deleteGarments(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
            this.garmentService.deleteGarments();
            return "redirect:/garments";
        }
        return "redirect:/error";
    }

    @GetMapping("/garments/createGarment")
    public String createGarment(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            return "createGarment";
        }
        return "redirect:/login";
    }

    @PostMapping("/garments/createGarment")
    public String createGarment(@RequestParam("garmentName") String garmentName, @RequestParam("type") String type, @RequestParam("url") String url){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (garmentName.length()!=0){
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
                this.garmentService.createGarment(new Garment(garmentName, url, type));
                return "redirect:/garments";
            }
        }
        return "redirect:/error";
    }

    @GetMapping("/garments/updateGarment/{id}")
    public String updateGarment(Model model, @PathVariable("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            Garment garment = this.garmentService.getGarmentById(id);
            model.addAttribute("garment", garment);
            return "updateGarment";
        }
        return "redirect:/error";
    }

    @PostMapping("/garments/modifySuccess")
    public String modifySuccess(@RequestParam("id") Long id, @RequestParam("garmentName") String garmentName, @RequestParam("type") String type, @RequestParam("url") String url){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))){
            Garment garment = this.garmentService.modifyGarment(id, new Garment(garmentName, url, type));
            if (garment==null){
                return "redirect:/error";
            }
            String returnvalue = "redirect:/garments/" + id;
            return returnvalue;
        }
        return "redirect:/error";
    }

    @GetMapping("/searchGarments")
    public String searchOutfit(Model model, @RequestParam String search){
        model.addAttribute("garments", this.querySearchGarment.searchByWord(search));
        return "showGarments";
    }

}
