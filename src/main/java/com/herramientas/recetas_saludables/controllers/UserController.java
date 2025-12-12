package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public String perfil(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String correo,
            Model model) {

        User usuario;

        if (userDetails != null) {
            // Usuario logueado
            usuario = userService.obtenerPorCorreo(userDetails.getUsername());
        } else if (correo != null) {
            // Usuario público vía parámetro
            usuario = userService.obtenerPorCorreo(correo);
        } else {
            // Ningún usuario, mostrar mensaje público
            usuario = null;
        }

        model.addAttribute("usuario", usuario);
        return "perfil"; // misma plantilla Thymeleaf
    }
}

// REST API Controllers para favoritos
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
class UserRestController {
    
    @Autowired
    private UserService userService;
    
    // Obtener usuario actual autenticado
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String correo = auth.getName();
        User user = userService.obtenerPorCorreo(correo);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    // Agregar/Remover receta de favoritos (toggle)
    @PostMapping("/favoritos/{recipeId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long recipeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        String correo = auth.getName();
        User user = userService.obtenerPorCorreo(correo);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        boolean isFavorite = user.getRecetasFavoritas().contains(recipeId);
        
        if (isFavorite) {
            user.getRecetasFavoritas().remove(recipeId);
        } else {
            user.getRecetasFavoritas().add(recipeId);
        }
        
        userService.guardar(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("favorito", !isFavorite);
        response.put("recipeId", recipeId);
        
        return ResponseEntity.ok(response);
    }
    
    // Obtener todos los favoritos del usuario
    @GetMapping("/favoritos")
    public ResponseEntity<?> getFavorites() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        String correo = auth.getName();
        User user = userService.obtenerPorCorreo(correo);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        return ResponseEntity.ok(user.getRecetasFavoritas());
    }
    
    // Verificar si una receta es favorita
    @GetMapping("/favoritos/{recipeId}")
    public ResponseEntity<?> isFavorite(@PathVariable Long recipeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        String correo = auth.getName();
        User user = userService.obtenerPorCorreo(correo);
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        boolean isFavorite = user.getRecetasFavoritas().contains(recipeId);
        Map<String, Object> response = new HashMap<>();
        response.put("recipeId", recipeId);
        response.put("favorito", isFavorite);
        
        return ResponseEntity.ok(response);
    }
}
