/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.indigo.prestamosLibros.repository;

import com.indigo.prestamosLibros.model.Usuario;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ANDRES GUTIERREZ
 */
public interface UsuarioRepository extends JpaRepository <Usuario, UUID> {
    Optional<Usuario> findByIdentificacion(String identificacion);
    
}
