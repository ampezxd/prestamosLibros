package com.indigo.prestamosLibros.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "Estructura estándar de respuesta para errores de la API (ej. validación, recursos no encontrados).")
public class ResponseError {

    @Schema(description = "Código de estado HTTP de la respuesta de error (ej. 400, 404, 500).", example = "400")
    private Integer status;

    @Schema(description = "Mensaje descriptivo del error ocurrido.", example = "El campo 'isbn' es obligatorio.")
    private String message;

    @Schema(description = "Fecha y hora en que ocurrió el error.", example = "2025-11-23T13:22:53")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;
}
