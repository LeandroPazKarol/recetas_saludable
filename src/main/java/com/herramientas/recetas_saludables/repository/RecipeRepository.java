package com.herramientas.recetas_saludables.repository;

import com.herramientas.recetas_saludables.model.DificultadEnum;
import com.herramientas.recetas_saludables.model.Ingredient;
import com.herramientas.recetas_saludables.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    // Búsqueda por nombre
    List<Recipe> findByNombreContainingIgnoreCase(String nombre);
    
    // Búsqueda por dificultad
    List<Recipe> findByDificultad(DificultadEnum dificultad);
    
       // Búsqueda por precio máximo (<=)
       List<Recipe> findByPrecioLessThanEqual(Integer maxPrecio);
    
    // Búsqueda por ingrediente
    @Query("SELECT r FROM Recipe r JOIN r.ingredientes i WHERE i.id = :ingredienteId")
    List<Recipe> findByIngrediente(@Param("ingredienteId") Long ingredienteId);
    
    // Búsqueda combinada: dificultad y presupuesto
       List<Recipe> findByDificultadAndPrecioLessThanEqual(DificultadEnum dificultad, Integer maxPrecio);
    
    // Búsqueda por ingrediente y dificultad
    @Query("SELECT r FROM Recipe r JOIN r.ingredientes i " +
           "WHERE i.id = :ingredienteId AND r.dificultad = :dificultad")
    List<Recipe> findByIngredienteAndDificultad(@Param("ingredienteId") Long ingredienteId, 
                                                 @Param("dificultad") DificultadEnum dificultad);
    
    // Búsqueda por ingrediente y precio máximo
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredientes i " +
           "WHERE i.id = :ingredienteId AND r.precio <= :maxPrecio")
    List<Recipe> findByIngredienteAndPrecioLessThanEqual(@Param("ingredienteId") Long ingredienteId, 
                                                  @Param("maxPrecio") Integer maxPrecio);
    
    // Búsqueda completa: ingrediente, dificultad y precio máximo
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredientes i " +
           "WHERE i.id = :ingredienteId AND r.dificultad = :dificultad AND r.precio <= :maxPrecio")
    List<Recipe> findByIngredienteAndDificultadAndPrecioLessThanEqual(
            @Param("ingredienteId") Long ingredienteId,
            @Param("dificultad") DificultadEnum dificultad,
            @Param("maxPrecio") Integer maxPrecio);
}
