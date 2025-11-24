/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.service;

import com.indigo.prestamosLibros.dto.PrestamoDetailResponse;
import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import com.indigo.prestamosLibros.model.Prestamo;
import com.indigo.prestamosLibros.model.Usuario;
import com.indigo.prestamosLibros.repository.PrestamoRepository;
import com.indigo.prestamosLibros.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ANDRES GUTIERREZ
 */

@Service
@RequiredArgsConstructor
public class PrestamoServiceImpl implements PrestamoService {

    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    private LocalDate calcularFechaDevolucion(int tipoUsuario) {
        int diasASumar;
        
        switch (tipoUsuario) {
            case 1:
                diasASumar = 10;
                break;
            case 2:
                diasASumar = 8;
                break;
            case 3:
                diasASumar = 7;
                break;
            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Tipo de usuario no permitido: " + tipoUsuario); //Manejar error 400
        }
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMaxima = fechaActual;
        
        int diasHabilesRestantes = diasASumar;
        
        while (diasHabilesRestantes > 0) {
            fechaMaxima = fechaMaxima.plusDays(1);
            DayOfWeek dia = fechaMaxima.getDayOfWeek();
            
            if (dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY) {
                diasHabilesRestantes--;
            }
        }
        return fechaMaxima;
    }
    
    @Override
    @Transactional
    public PrestamoResponse crearPrestamo(PrestamoRequest request) {
        //Crear el usuario
        Usuario usuario = usuarioRepository
                .findByIdentificacion(request.identificacionUsuario())
                .orElseGet(()->{
                    //como el usuario no existe, lo crea
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setId(UUID.randomUUID());
                    nuevoUsuario.setIdentificacion(request.identificacionUsuario());
                    nuevoUsuario.setTipoUsuario(request.tipoUsuario());
                    return usuarioRepository.save(nuevoUsuario);
                });
        //Usuario invitado
        if (usuario.getTipoUsuario() == 3) {
            long prestamosActivos = prestamoRepository.countByUsuario_Identificacion(usuario.getIdentificacion());
            
            if (prestamosActivos >= 1L) {
                //retornar error 400
                throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "El usuario ya tiene un libro prestado");
            }
        }
        //Calcular fecha
        LocalDate fechaDevolucion = calcularFechaDevolucion(usuario.getTipoUsuario());
        
        //Crear el prestamo
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setId(UUID.randomUUID());
        nuevoPrestamo.setIsbn(request.isbn());
        nuevoPrestamo.setFechaMaximaDevolucion(fechaDevolucion);
        nuevoPrestamo.setUsuario(usuario);
        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);
        
        //Respuesta
        return new PrestamoResponse(
        prestamoGuardado.getId(),
        prestamoGuardado.getFechaMaximaDevolucion());
    }

    @Override
    public PrestamoDetailResponse obtenerPrestamo (UUID id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El préstamo no existe"));

        return new PrestamoDetailResponse(
            prestamo.getId(),
            prestamo.getIsbn(),
            prestamo.getUsuario().getIdentificacion(),
            prestamo.getUsuario().getTipoUsuario(),
            prestamo.getFechaMaximaDevolucion()
        );
    }
}