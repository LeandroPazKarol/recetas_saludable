package com.herramientas.recetas_saludables.controllers;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class mainControllerTest {

    @Test
    void testHome() {
        MainController controller = new MainController();
        assertEquals("index", controller.home());
    }
}