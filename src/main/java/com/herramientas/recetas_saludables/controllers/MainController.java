package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/index", "/index.html"})
    public String home(){
        return "index";
    }

    @GetMapping({"/login", "/login.html"})
    public String login(){
        return "login";
    }

    @GetMapping({"/registro", "/registro.html"})
    public String registro(Model model){
        model.addAttribute("usuario",new User());
        return "registro";
    }

    @GetMapping({"/buscar", "/buscar.html"})
    public String buscar(Model model){
        model.addAttribute("usuario",new User());
        return "buscar";
    }
    @GetMapping({"/recetas", "/recetas.html"})
    public String recetas(Model model){
        model.addAttribute("usuario",new User());
        return "recetas";
    }
    @GetMapping({"/ideas", "/ideas.html"})
    public String ideas(Model model){
        model.addAttribute("usuario",new User());
        return "ideas";
    }


}
