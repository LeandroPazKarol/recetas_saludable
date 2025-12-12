package com.herramientas.recetas_saludables.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;

    @Column(unique = true, nullable = false)
    private String correo;

    private String contrasena;
    private String pais;
    private String rol;

    @ElementCollection
    @CollectionTable(name = "usuario_favoritos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "receta_id")
    private Set<Long> recetasFavoritas = new HashSet<>();
}