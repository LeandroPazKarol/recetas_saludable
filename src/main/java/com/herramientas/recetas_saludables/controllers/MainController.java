package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model model){
        model.addAttribute("usuario",new User());
        return "registro";
    }

    @GetMapping("/buscar")
    public String buscar(Model model){
        model.addAttribute("usuario",new User());
        return "buscar";
    }
    @GetMapping("/recetas")
    public String recetas(Model model){
        model.addAttribute("usuario",new User());
        return "recetas";
    }
    @GetMapping("/ideas")
    public String ideas(Model model){
        model.addAttribute("usuario",new User());
        return "ideas";
    }


}
