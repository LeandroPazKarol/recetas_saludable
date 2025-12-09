package com.herramientas.recetas_saludables.services;

import com.herramientas.recetas_saludables.model.Ingredient;
import com.herramientas.recetas_saludables.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    // Obtener todos los ingredientes
    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
    
    // Obtener ingrediente por ID
    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.findById(id);
    }
    
    // Obtener ingrediente por nombre
    public Optional<Ingredient> getIngredientByNombre(String nombre) {
        return ingredientRepository.findByNombreIgnoreCase(nombre);
    }
    
    // Buscar ingredientes por nombre (parcial)
    public List<Ingredient> searchByNombre(String nombre) {
        return ingredientRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Crear o actualizar ingrediente
    public Ingredient saveIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }
    
    // Eliminar ingrediente
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}
