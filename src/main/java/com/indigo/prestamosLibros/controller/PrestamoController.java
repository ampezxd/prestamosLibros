/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.controller;

import com.indigo.prestamosLibros.dto.PrestamoDetailResponse;
import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import com.indigo.prestamosLibros.service.PrestamoService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ANDRES GUTIERREZ
 */
@RestController
@RequestMapping("/api/prestamo")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamoService;
    
    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@RequestBody PrestamoRequest request) {
        PrestamoResponse response = prestamoService.crearPrestamo(request);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{idPrestamo}")
    public ResponseEntity<PrestamoDetailResponse> consultarPrestamo(@PathVariable UUID idPrestamo) {
        PrestamoDetailResponse response = prestamoService.obtenerPrestamo(idPrestamo);
        return ResponseEntity.ok(response);
}
}
