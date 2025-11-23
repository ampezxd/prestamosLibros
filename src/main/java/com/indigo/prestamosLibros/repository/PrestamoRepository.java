/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.indigo.prestamosLibros.repository;

import com.indigo.prestamosLibros.model.Prestamo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public interface PrestamoRepository extends JpaRepository<Prestamo, UUID>{
    boolean existsByIdentificacionUsuario(String identificacionUsuario);
    
}
