/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.indigo.prestamosLibros.controller;

import com.indigo.prestamosLibros.dto.PrestamoDetailResponse;
import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import com.indigo.prestamosLibros.dto.ResponseError;
import com.indigo.prestamosLibros.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Tag(name = "Préstamos", description = "Endpoints para la gestión de préstamos de libros.")
@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor
public class PrestamoController {

    private final PrestamoService prestamoService;

    // --- ENDPOINT POST: CREAR PRÉSTAMO ---

    @Operation(summary = "Registra un nuevo préstamo de libro",
            description = "Valida los datos de la solicitud y registra un nuevo préstamo, devolviendo el ID y la fecha máxima de devolución.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos necesarios para registrar el préstamo (ISBN, Identificación, Tipo de Usuario).",
            required = true,
            content = @Content(schema = @Schema(implementation = PrestamoRequest.class)))
    @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PrestamoResponse.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o JSON mal formado.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    @ApiResponse(responseCode = "409", description = "Violación de la integridad de los datos (ej. usuario ya tiene el máximo de libros).",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Validated @RequestBody PrestamoRequest request) {
        PrestamoResponse response = prestamoService.crearPrestamo(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // --- ENDPOINT GET: CONSULTAR PRÉSTAMO POR ID ---

    @Operation(summary = "Consulta un préstamo por su ID (UUID)",
            description = "Busca un préstamo específico en el sistema utilizando su identificador único universal.")
    @ApiResponse(responseCode = "200", description = "Préstamo encontrado y devuelto.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PrestamoDetailResponse.class)))
    @ApiResponse(responseCode = "404", description = "El préstamo con el ID especificado no fue encontrado.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    @ApiResponse(responseCode = "400", description = "El formato del ID proporcionado es inválido (no es un UUID válido).",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseError.class)))
    @GetMapping("/{idPrestamo}")
    public ResponseEntity<PrestamoDetailResponse> consultarPrestamo(@PathVariable UUID idPrestamo) {
        PrestamoDetailResponse response = prestamoService.obtenerPrestamo(idPrestamo);
        return ResponseEntity.ok(response);
    }
}
