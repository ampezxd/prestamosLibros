package com.indigo.prestamosLibros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indigo.prestamosLibros.dto.PrestamoDetailResponse;
import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import com.indigo.prestamosLibros.service.PrestamoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author ANDRES GUTIERREZ
 */

import java.time.LocalDate;
import java.util.UUID;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerIntegrationTest {

    private final String API_ENDPOINT = "/api/v1/prestamos";

    // Inyección de MockMvc para simular las peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    // Inyección de ObjectMapper para convertir objetos Java a JSON y viceversa
    @Autowired
    private ObjectMapper objectMapper;

    // Mockea la capa de servicio, ya que la probaremos aisladamente
    @MockBean
    private PrestamoService prestamoService;

    // Datos de prueba
    private PrestamoRequest validRequest;
    private PrestamoResponse prestamoResponse;
    private PrestamoDetailResponse prestamoDetailResponse;
    private final UUID PRESTAMO_UUID = UUID.randomUUID();
    private final LocalDate FECHA_DEVOLUCION = LocalDate.now().plusDays(10);


    @BeforeEach
    void setUp() {
        // Objeto de Petición válido (asumiendo que las reglas de validación se cumplen)
        validRequest = new PrestamoRequest("978-3161484100", "1234567890", 1);

        // Objetos de Respuesta esperados
        prestamoResponse = new PrestamoResponse(PRESTAMO_UUID, FECHA_DEVOLUCION);
        prestamoDetailResponse = new PrestamoDetailResponse(
                PRESTAMO_UUID, validRequest.isbn(), validRequest.identificacionUsuario(),
                validRequest.tipoUsuario(), FECHA_DEVOLUCION
        );
    }

    // =========================================================================
    // 1. Test POST /api/v1/prestamos (Creación Exitosa)
    // =========================================================================

    @Test
    void crearPrestamo_shouldReturn201_and_PrestamoResponse() throws Exception {
        // ARRANGE: Configurar el mock para que devuelva la respuesta esperada al llamar al servicio
        when(prestamoService.crearPrestamo(any(PrestamoRequest.class))).thenReturn(prestamoResponse);

        // ACT & ASSERT: Simular la petición POST
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))) // Cuerpo de la petición

                // ASSERT 1: Verificar el código de estado HTTP
                .andExpect(status().isCreated()); // Espera 201 Created

        // ASSERT 3: Verificar que el método del servicio fue llamado
        verify(prestamoService, times(1)).crearPrestamo(any(PrestamoRequest.class));
    }

    @Test
    void consultarPrestamo_shouldReturn200() throws Exception {
        // ARRANGE: Configurar el mock para que devuelva los detalles esperados
        when(prestamoService.obtenerPrestamo(PRESTAMO_UUID)).thenReturn(prestamoDetailResponse);

        // ACT & ASSERT: Simular la petición GET
        mockMvc.perform(get(API_ENDPOINT + "/{id}", PRESTAMO_UUID)
                        .contentType(MediaType.APPLICATION_JSON))

                // ASSERT 1: Verificar el código de estado HTTP
                .andExpect(status().isOk()); // Espera 200 OK

        // ASSERT 3: Verificar que el método del servicio fue llamado
        verify(prestamoService, times(1)).obtenerPrestamo(PRESTAMO_UUID);
    }
}