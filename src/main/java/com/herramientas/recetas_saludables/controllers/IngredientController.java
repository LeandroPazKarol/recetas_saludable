package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.Ingredient;
import com.herramientas.recetas_saludables.services.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
@CrossOrigin(origins = "*")
public class IngredientController {
    
    @Autowired
    private IngredientService ingredientService;
    
    // Obtener todos los ingredientes
    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }
    
    // Obtener ingrediente por ID
    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id).orElse(null);
    }
    
    // Crear ingrediente
    @PostMapping
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.saveIngredient(ingredient);
    }
    
    // Actualizar ingrediente
    @PutMapping("/{id}")
    public Ingredient updateIngredient(@PathVariable Long id, @RequestBody Ingredient ingredient) {
        ingredient.setId(id);
        return ingredientService.saveIngredient(ingredient);
    }
    
    // Eliminar ingrediente
    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
    }
    
    // Buscar ingredientes por nombre (parcial)
    @GetMapping("/buscar")
    public List<Ingredient> searchByNombre(@RequestParam String nombre) {
        return ingredientService.searchByNombre(nombre);
    }
    
    // Obtener ingrediente por nombre exacto
    @GetMapping("/nombre")
    public Ingredient getIngredientByNombre(@RequestParam String nombre) {
        return ingredientService.getIngredientByNombre(nombre).orElse(null);
    }
}
