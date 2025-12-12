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

    //Obtener datos del usuario autenticado
    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails == null) {
            // Si no hay usuario autenticado, redirigir al login
            return "redirect:/login";
        }

        // Obtener el usuario actual por su correo (username)
        User usuario = userService.obtenerPorCorreo(userDetails.getUsername());

        if (usuario == null) {
            // Si no se encuentra el usuario, redirigir al login
            return "redirect:/login";
        }

        // Pasar el usuario al modelo
        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    //Actualizar datos del perfil
    @PostMapping("/actualizar")
    public String actualizarPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam String pais,
                                   Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        User usuario = userService.obtenerPorCorreo(userDetails.getUsername());

        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setTelefono(telefono);
            usuario.setPais(pais);
            userService.guardar(usuario);

            model.addAttribute("success", "Perfil actualizado correctamente");
        }

        return "redirect:/usuario/perfil?updated=true";
    }

    //Cambiar contraseña
    @PostMapping("/cambiar-contrasena")
    public String cambiarContrasena(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam String contrasenaActual,
                                    @RequestParam String contrasenaNueva,
                                    @RequestParam String confirmarContrasena,
                                    Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        // Verificar que las contraseñas nuevas coincidan
        if (!contrasenaNueva.equals(confirmarContrasena)) {
            return "redirect:/usuario/perfil?error=password-mismatch";
        }

        User usuario = userService.obtenerPorCorreo(userDetails.getUsername());

        if (usuario != null) {
            // Verificar la contraseña actual
            if (!passwordEncoder.matches(contrasenaActual, usuario.getContrasena())) {
                return "redirect:/usuario/perfil?error=wrong-password";
            }

            // Actualizar la contraseña
            usuario.setContrasena(passwordEncoder.encode(contrasenaNueva));
            userService.guardar(usuario);
        }

        return "redirect:/usuario/perfil?password-changed=true";
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
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userService.obtenerPorCorreo(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    // Agregar/Remover receta de favoritos (toggle)
    @PostMapping("/favoritos/{recipeId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long recipeId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        User user = userService.obtenerPorCorreo(userDetails.getUsername());
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
    public ResponseEntity<?> getFavorites(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        User user = userService.obtenerPorCorreo(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        
        return ResponseEntity.ok(user.getRecetasFavoritas());
    }
    
    // Verificar si una receta es favorita
    @GetMapping("/favoritos/{recipeId}")
    public ResponseEntity<?> isFavorite(@PathVariable Long recipeId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("No autenticado");
        }
        
        User user = userService.obtenerPorCorreo(userDetails.getUsername());
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
