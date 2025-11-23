/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author ANDRES GUTIERREZ
 */

@Schema(description = "DTO de respuesta que contiene todos los detalles de un préstamo ya registrado.")
public record PrestamoDetailResponse (

        // 2. Describe cada componente
        @Schema(description = "Identificador único universal (UUID) del préstamo.")
        UUID id,

        @Schema(description = "Código ISBN del libro prestado.")
        String isbn,

        @Schema(description = "Identificación única del usuario que recibió el préstamo (ej. DNI, Cédula).")
        String identificacionUsuario,

        @Schema(description = "Tipo de usuario que realizó el préstamo (ej. 1: Afiliado, 2: Empleado).")
        Integer tipoUsuario,

        @Schema(description = "Fecha límite de devolución del libro (YYYY-MM-DD).")
        LocalDate fechaMaximaDevolucion) {

}