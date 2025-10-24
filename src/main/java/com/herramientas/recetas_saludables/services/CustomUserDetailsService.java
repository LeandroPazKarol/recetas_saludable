package com.herramientas.recetas_saludables.services;

import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCorreo())
                .password(user.getContrasena())
                .roles(user.getRol()) // debe ser "USER", "ADMIN", etc.
                .build();
    }
}