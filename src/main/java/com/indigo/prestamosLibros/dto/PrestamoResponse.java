/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.dto;

import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public record PrestamoResponse (
        UUID id,
        LocalDate fechaMaximaDevolucion) {
    
}
