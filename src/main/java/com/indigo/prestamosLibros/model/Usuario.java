/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
 
    @Id
    private UUID id;

    @Column(length = 10, nullable = false, unique = true)
    private String identificacion;
    
    @Column(nullable = false)
    private Integer tipoUsuario;
    
    @OneToMany(mappedBy = "usuario")
    private List<Prestamo> prestamos; 
    
}
