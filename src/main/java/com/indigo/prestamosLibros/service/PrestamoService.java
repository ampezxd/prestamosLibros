/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.indigo.prestamosLibros.service;

import com.indigo.prestamosLibros.dto.PrestamoDetailResponse;
import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import java.util.UUID;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public interface PrestamoService {
    PrestamoResponse crearPrestamo(PrestamoRequest request);
    
    PrestamoDetailResponse obtenerPrestamo(UUID id);
}
