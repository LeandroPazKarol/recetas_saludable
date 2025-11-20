package com.herramientas.recetas_saludables.model;


@Entity
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String contrasena;
    private String pais;

    private String rol;

}
