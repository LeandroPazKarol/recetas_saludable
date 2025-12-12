package com.herramientas.recetas_saludables.controllers;

import com.herramientas.recetas_saludables.model.DificultadEnum;
import com.herramientas.recetas_saludables.model.Ingredient;
import com.herramientas.recetas_saludables.model.Recipe;
import com.herramientas.recetas_saludables.model.User;
import com.herramientas.recetas_saludables.services.IngredientService;
import com.herramientas.recetas_saludables.services.RecipeService;
import com.herramientas.recetas_saludables.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
@CrossOrigin(origins = "*")
public class RecipeController {
    
    @Autowired
    private RecipeService recipeService;
    
    @Autowired
    private IngredientService ingredientService;
    
    @Autowired
    private UserService userService;
    
    // Obtener todas las recetas
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }
    
    // Obtener receta por ID
    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id).orElse(null);
    }
    
    // Crear receta
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe, 
                              @RequestParam(required = false) List<Long> ingredienteId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        // Asignar ingredientes si se proporcionan
        if (ingredienteId != null && !ingredienteId.isEmpty()) {
            for (Long id : ingredienteId) {
                Ingredient ing = ingredientService.getIngredientById(id).orElse(null);
                if (ing != null) {
                    recipe.getIngredientes().add(ing);
                }
            }
        }
        
        // Si existe un usuario autenticado, asignar su ID a la receta
        if (userDetails != null) {
            User user = userService.obtenerPorCorreo(userDetails.getUsername());
            if (user != null) {
                recipe.setUsuarioId(user.getId());
            }
        }
        return recipeService.saveRecipe(recipe);
    }
    
    // Actualizar receta
    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        recipe.setId(id);
        return recipeService.saveRecipe(recipe);
    }
    
    // Eliminar receta
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
    
    // Búsqueda flexible por múltiples criterios
    @GetMapping("/buscar")
    public List<Recipe> search(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) DificultadEnum dificultad,
            @RequestParam(required = false) Integer maxPrecio,
            @RequestParam(required = false) Long ingredienteId) {
        return recipeService.searchWithFlexibleFilters(nombre, dificultad, maxPrecio, ingredienteId);
    }
    
    // Búsqueda por nombre
    @GetMapping("/nombre")
    public List<Recipe> searchByNombre(@RequestParam String nombre) {
        return recipeService.searchByNombre(nombre);
    }
    
    // Búsqueda por dificultad
    @GetMapping("/dificultad")
    public List<Recipe> searchByDificultad(@RequestParam DificultadEnum dificultad) {
        return recipeService.searchByDificultad(dificultad);
    }
    
    // Búsqueda por precio máximo
    @GetMapping("/presupuesto")
    public List<Recipe> searchByPresupuesto(@RequestParam Integer maxPrecio) {
        return recipeService.searchByPrecioMax(maxPrecio);
    }
    
    // Búsqueda por ingrediente
    @GetMapping("/ingrediente")
    public List<Recipe> searchByIngrediente(@RequestParam Long ingredienteId) {
        return recipeService.searchByIngrediente(ingredienteId);
    }
    
    // Búsqueda por dificultad y precio máximo
    @GetMapping("/dificultad-presupuesto")
    public List<Recipe> searchByDificultadAndPresupuesto(
            @RequestParam DificultadEnum dificultad,
            @RequestParam Integer maxPrecio) {
        return recipeService.searchByDificultadAndPrecio(dificultad, maxPrecio);
    }
    
    // Búsqueda por ingrediente y dificultad
    @GetMapping("/ingrediente-dificultad")
    public List<Recipe> searchByIngredienteAndDificultad(
            @RequestParam Long ingredienteId,
            @RequestParam DificultadEnum dificultad) {
        return recipeService.searchByIngredienteAndDificultad(ingredienteId, dificultad);
    }
    
    // Búsqueda por ingrediente y precio máximo
    @GetMapping("/ingrediente-presupuesto")
    public List<Recipe> searchByIngredienteAndPresupuesto(
            @RequestParam Long ingredienteId,
            @RequestParam Integer maxPrecio) {
        return recipeService.searchByIngredienteAndPrecio(ingredienteId, maxPrecio);
    }
    
    // Búsqueda completa: ingrediente, dificultad y precio máximo
    @GetMapping("/filtros-completos")
    public List<Recipe> searchByAllFilters(
            @RequestParam Long ingredienteId,
            @RequestParam DificultadEnum dificultad,
            @RequestParam Integer maxPrecio) {
        return recipeService.searchByFilters(ingredienteId, dificultad, maxPrecio);
    }
}
