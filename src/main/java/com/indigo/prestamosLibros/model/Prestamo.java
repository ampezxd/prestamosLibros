/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@Entity
@Table(name = "prestamo")
@Data

@NoArgsConstructor

@AllArgsConstructor
public class Prestamo {
    
    @Id
    private UUID id;
    
    private String isbn;

    private LocalDate fechaMaximaDevolucion;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    }
