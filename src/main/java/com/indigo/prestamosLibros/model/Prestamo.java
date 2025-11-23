/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@Entity
public class Prestamo {
    
    @Id
    private UUID id;
    
    private String isbn;

    private LocalDate fechaMaximaDevolucion;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    }
