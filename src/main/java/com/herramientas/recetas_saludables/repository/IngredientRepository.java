package com.herramientas.recetas_saludables.repository;

import com.herramientas.recetas_saludables.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByNombreIgnoreCase(String nombre);
    
    List<Ingredient> findByNombreContainingIgnoreCase(String nombre);
}
