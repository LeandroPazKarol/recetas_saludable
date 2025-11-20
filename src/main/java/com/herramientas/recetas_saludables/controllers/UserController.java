package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarioLista", userService.listarTodos());
        return "index";
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute User usuario) {

        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setRol("USER");

        userService.guardar(usuario);
        return "redirect:/login";
    }

    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User usuarioActual = userService.obtenerPorCorreo(userDetails.getUsername());
        model.addAttribute("usuario", usuarioActual);
        return "perfil"; // Thymeleaf template
    }
}
