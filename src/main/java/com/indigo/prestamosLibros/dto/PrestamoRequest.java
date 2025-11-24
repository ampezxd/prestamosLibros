/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public record PrestamoRequest (

        // El ISBN no puede ser nulo ni una cadena vacía/solo espacios
        @Schema(description = "Código ISBN del libro que se desea prestar.",
                example = "978-8441542157",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1) // Añade la restricción de longitud mínima
        @NotBlank(message = "El ISBN es obligatorio y no puede estar vacío.")
        String isbn,

        // La identificación del usuario es obligatoria y no puede estar vacía
        @Schema(description = "Identificación única del usuario (ej. Cédula, DNI).",
                example = "1098765432",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1)
        @NotBlank(message = "La identificación del usuario es obligatoria.")
        String identificacionUsuario,

        // El tipo de usuario debe ser un valor no nulo y >= 1
        @Schema(description = "Tipo de usuario que solicita el préstamo (1: Afiliado, 2: Empleado).",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "1") // Añade la restricción de valor mínimo
        @NotNull(message = "El tipo de usuario es obligatorio.")
        @Min(value = 1, message = "El tipo de usuario debe ser 1 o superior.")
        Integer tipoUsuario) {}
