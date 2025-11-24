/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@Schema(description = "DTO de respuesta concisa que contiene los identificadores clave y la fecha límite de un nuevo préstamo.")
public record PrestamoResponse (

        @Schema(description = "Identificador único universal (UUID) asignado al nuevo préstamo.",
                example = "a1b2c3d4-e5f6-7890-1234-567890abcdef",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "uuid")
        @NotNull(message = "El ID del préstamo no puede ser nulo.")
        UUID id,

        @Schema(description = "Fecha máxima de devolución del libro (debe ser hoy o posterior).",
                example = "2025-12-05",
                requiredMode = Schema.RequiredMode.REQUIRED,
                format = "date")
        @NotNull(message = "La fecha máxima de devolución es obligatoria.")
        @FutureOrPresent(message = "La fecha máxima de devolución debe ser hoy o en el futuro.")
        LocalDate fechaMaximaDevolucion) {}