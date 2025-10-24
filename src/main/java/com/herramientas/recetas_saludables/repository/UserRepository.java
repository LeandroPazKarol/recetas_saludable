package com.herramientas.recetas_saludables.repository;

import com.herramientas.recetas_saludables.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByCorreo(String correo);
}
