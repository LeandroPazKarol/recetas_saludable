package com.herramientas.recetas_saludables.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recetas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    private Integer tiempoPreparacion; // minutos
    
    @Enumerated(EnumType.STRING)
    private DificultadEnum dificultad;
    
    private Integer precio; // precio aproximado o presupuesto (valor entero)
    
    @ManyToMany
    @JoinTable(
        name = "receta_ingredientes",
        joinColumns = @JoinColumn(name = "receta_id"),
        inverseJoinColumns = @JoinColumn(name = "ingrediente_id")
    )
    private Set<Ingredient> ingredientes = new HashSet<>();
    
    private String imagenUrl;
    
    @Column(columnDefinition = "TEXT")
    private String instrucciones;
}