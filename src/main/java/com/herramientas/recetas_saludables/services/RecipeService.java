package com.herramientas.recetas_saludables.services;

import com.herramientas.recetas_saludables.model.DificultadEnum;
import com.herramientas.recetas_saludables.model.Ingredient;
import com.herramientas.recetas_saludables.model.Recipe;
import com.herramientas.recetas_saludables.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    // Obtener todas las recetas
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    
    // Obtener receta por ID
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }
    
    // Crear o actualizar receta
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
    
    // Eliminar receta
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
    
    // Búsqueda por nombre
    public List<Recipe> searchByNombre(String nombre) {
        return recipeRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Búsqueda por dificultad
    public List<Recipe> searchByDificultad(DificultadEnum dificultad) {
        return recipeRepository.findByDificultad(dificultad);
    }
    
    // Búsqueda por precio máximo
    public List<Recipe> searchByPrecioMax(Integer maxPrecio) {
        if (maxPrecio == null) return getAllRecipes();
        return recipeRepository.findByPrecioLessThanEqual(maxPrecio);
    }
    
    // Búsqueda por ingrediente
    public List<Recipe> searchByIngrediente(Long ingredienteId) {
        return recipeRepository.findByIngrediente(ingredienteId);
    }
    
    // Búsqueda por dificultad y precio máximo
    public List<Recipe> searchByDificultadAndPrecio(DificultadEnum dificultad, Integer maxPrecio) {
        return recipeRepository.findByDificultadAndPrecioLessThanEqual(dificultad, maxPrecio);
    }
    
    // Búsqueda por ingrediente y dificultad
    public List<Recipe> searchByIngredienteAndDificultad(Long ingredienteId, DificultadEnum dificultad) {
        return recipeRepository.findByIngredienteAndDificultad(ingredienteId, dificultad);
    }
    
    // Búsqueda por ingrediente y precio máximo
    public List<Recipe> searchByIngredienteAndPrecio(Long ingredienteId, Integer maxPrecio) {
        return recipeRepository.findByIngredienteAndPrecioLessThanEqual(ingredienteId, maxPrecio);
    }
    
    // Búsqueda completa: ingrediente, dificultad y precio máximo
    public List<Recipe> searchByFilters(Long ingredienteId, DificultadEnum dificultad, Integer maxPrecio) {
        return recipeRepository.findByIngredienteAndDificultadAndPrecioLessThanEqual(ingredienteId, dificultad, maxPrecio);
    }
    
    // Búsqueda flexible: soporta cualquier combinación de filtros
    public List<Recipe> searchWithFlexibleFilters(String nombre, DificultadEnum dificultad, Integer maxPrecio, Long ingredienteId) {
        // Implementación flexible: si todos los filtros son nulos, retorna todas
        if (nombre == null && dificultad == null && maxPrecio == null && ingredienteId == null) {
            return getAllRecipes();
        }
        
        // Si solo hay dificultad
        if (nombre == null && dificultad != null && maxPrecio == null && ingredienteId == null) {
            return searchByDificultad(dificultad);
        }
        
        // Si solo hay precio máximo
        if (nombre == null && dificultad == null && maxPrecio != null && ingredienteId == null) {
            return searchByPrecioMax(maxPrecio);
        }
        
        // Si solo hay ingrediente
        if (nombre == null && dificultad == null && maxPrecio == null && ingredienteId != null) {
            return searchByIngrediente(ingredienteId);
        }
        
        // Si hay dificultad y precio máximo
        if (nombre == null && dificultad != null && maxPrecio != null && ingredienteId == null) {
            return searchByDificultadAndPrecio(dificultad, maxPrecio);
        }
        
        // Si hay ingrediente y dificultad
        if (nombre == null && dificultad != null && maxPrecio == null && ingredienteId != null) {
            return searchByIngredienteAndDificultad(ingredienteId, dificultad);
        }
        
        // Si hay ingrediente y precio máximo
        if (nombre == null && dificultad == null && maxPrecio != null && ingredienteId != null) {
            return searchByIngredienteAndPrecio(ingredienteId, maxPrecio);
        }
        
        // Si hay todos los filtros
        if (nombre == null && dificultad != null && maxPrecio != null && ingredienteId != null) {
            return searchByFilters(ingredienteId, dificultad, maxPrecio);
        }
        
        // Si hay nombre, lo priorizamos
        if (nombre != null && !nombre.isEmpty()) {
            return searchByNombre(nombre);
        }
        
        return getAllRecipes();
    }
}
