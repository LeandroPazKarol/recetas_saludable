package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    public String home(){
        return "index";
    }

    @GetMapping({"/login", "/login.html"})
    public String login(){
        return "login";
    }

    public String registro(Model model){
        model.addAttribute("usuario",new User());
        return "registro";
    }

    public String buscar(Model model){
        model.addAttribute("usuario",new User());
        return "buscar";
    }
    public String recetas(Model model){
        model.addAttribute("usuario",new User());
        return "recetas";
    }
    public String ideas(Model model){
        model.addAttribute("usuario",new User());
        return "ideas";
    }


}
