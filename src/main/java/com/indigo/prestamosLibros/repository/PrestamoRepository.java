/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.indigo.prestamosLibros.repository;

import com.indigo.prestamosLibros.model.Prestamo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, UUID>{
    //Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario);

    public long countByUsuario_Identificacion(String identificacion);
    
}
