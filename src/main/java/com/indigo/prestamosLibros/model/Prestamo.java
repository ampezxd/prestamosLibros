/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    // El ID es generado automáticamente y no debería ser nulo
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 1. ISBN: Debe existir y no estar vacío. Asumiendo una longitud máxima típica.
    @NotBlank(message = "{prestamo.isbn.notblank}")
    @Size(max = 20, message = "{prestamo.isbn.size}")
    private String isbn;

    // 2. Fecha Máxima Devolución: No puede ser nula y debe ser hoy o futura.
    @NotNull(message = "{prestamo.fechaMaximaDevolucion.notnull}")
    @FutureOrPresent(message = "{prestamo.fechaMaximaDevolucion.futureorpresent}")
    private LocalDate fechaMaximaDevolucion;

    // 3. Relación Usuario: Debe existir (no nula) y la columna debe ser obligatoria
    // Nota: El campo 'usuario' representa la relación, la 'identificacionUsuario'
    // se obtiene a través de esta relación o se pasa en el DTO de entrada.
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "{prestamo.usuario.notnull}")
    private Usuario usuario;
}
