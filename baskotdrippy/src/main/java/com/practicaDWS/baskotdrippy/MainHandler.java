package com.practicaDWS.baskotdrippy;

import com.practicaDWS.baskotdrippy.queries.QuerySearchGarment;
import com.practicaDWS.baskotdrippy.queries.QuerySearchOutfit;
import com.practicaDWS.baskotdrippy.queries.QuerySearchUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainHandler {

    @Autowired
    QuerySearchUser querySearchUser;
    @Autowired
    QuerySearchGarment querySearchGarment;
    @Autowired
    QuerySearchOutfit querySearchOutfit;

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/search")
    public String generalSearch(Model model, @RequestParam() String search){

        model.addAttribute("users", this.querySearchUser.searchByWord(search));
        model.addAttribute("outfits", this.querySearchOutfit.searchByWord(search));
        model.addAttribute("garments", this.querySearchGarment.searchByWord(search));
        
        return "showEverything";
    }

}
