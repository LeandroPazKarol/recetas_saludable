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
