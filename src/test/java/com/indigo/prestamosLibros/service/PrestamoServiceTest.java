package com.indigo.prestamosLibros.service;

import com.indigo.prestamosLibros.dto.PrestamoRequest;
import com.indigo.prestamosLibros.dto.PrestamoResponse;
import com.indigo.prestamosLibros.model.Prestamo;
import com.indigo.prestamosLibros.model.Usuario;
import com.indigo.prestamosLibros.repository.PrestamoRepository;
import com.indigo.prestamosLibros.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * Pruebas unitarias para la lógica de negocio en PrestamoServiceImpl.
 * Se utiliza Mockito para simular los repositorios y verificar la lógica pura.
 * Cubre el cálculo de fechas, la gestión de usuarios existentes/nuevos y el límite del invitado.
 */
@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    // Constantes para el tipo de usuario
    private static final int TIPO_AFILIADO = 1;
    private static final int TIPO_EMPLEADO = 2;
    private static final int TIPO_INVITADO = 3;
    private static final String ISBN_TEST = "978-607-314-577-7";

    // Usuarios de prueba (usados para simular usuarios ya existentes en la DB)
    private Usuario afiliadoExistente;
    private Usuario empleadoExistente;
    private Usuario invitadoExistente;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        // Configuramos usuarios con datos base para simular la BD

        afiliadoExistente = new Usuario();
        afiliadoExistente.setId(UUID.randomUUID());
        afiliadoExistente.setIdentificacion("1111");
        afiliadoExistente.setTipoUsuario(TIPO_AFILIADO);

        empleadoExistente = new Usuario();
        empleadoExistente.setId(UUID.randomUUID());
        empleadoExistente.setIdentificacion("2222");
        empleadoExistente.setTipoUsuario(TIPO_EMPLEADO);

        invitadoExistente = new Usuario();
        invitadoExistente.setId(UUID.randomUUID());
        invitadoExistente.setIdentificacion("3333");
        invitadoExistente.setTipoUsuario(TIPO_INVITADO);
    }

    /**
     * Prueba: Afiliado (Tipo 1) -> 10 días hábiles.
     * El usuario ya existe en la BD.
     */
    @Test
    void testAfiliadoExistenteDiezDiasHabiles() {
        // 1. Datos
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, "1111", TIPO_AFILIADO);

        // 2. Configuración de Mocks: El usuario existe.
        when(usuarioRepository.findByIdentificacion("1111")).thenReturn(Optional.of(afiliadoExistente));

        // Simula que se guarda cualquier préstamo y retorna el Prestamo para verificación.
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo prestamo = invocation.getArgument(0);
            prestamo.setId(UUID.randomUUID()); // Simula la generación de ID
            assertEquals(10, calcularDiasHabiles(LocalDate.now(), prestamo.getFechaMaximaDevolucion()),
                    "Debe haber 10 días hábiles entre hoy y la fecha máxima.");
            return prestamo;
        });

        // 3. Ejecución
        PrestamoResponse resultado = prestamoService.crearPrestamo(request);

        // 4. Verificación
        LocalDate fechaEsperada = calcularFechaHabil(LocalDate.now(), 10);
        assertNotNull(resultado);
        assertEquals(fechaEsperada, resultado.fechaMaximaDevolucion(),
                "La fecha de devolución para un afiliado debe ser 10 días hábiles.");

        // El repositorio de usuario NO debe ser llamado para guardar un nuevo usuario
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Prueba: Empleado (Tipo 2) -> 8 días hábiles.
     * El usuario ya existe en la BD.
     */
    @Test
    void testEmpleadoExistenteOchoDiasHabiles() {
        // 1. Datos
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, "2222", TIPO_EMPLEADO);

        // 2. Configuración de Mocks
        when(usuarioRepository.findByIdentificacion("2222")).thenReturn(Optional.of(empleadoExistente));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo prestamo = invocation.getArgument(0);
            prestamo.setId(UUID.randomUUID());
            assertEquals(8, calcularDiasHabiles(LocalDate.now(), prestamo.getFechaMaximaDevolucion()),
                    "Debe haber 8 días hábiles entre hoy y la fecha máxima.");
            return prestamo;
        });

        // 3. Ejecución
        PrestamoResponse resultado = prestamoService.crearPrestamo(request);

        // 4. Verificación
        LocalDate fechaEsperada = calcularFechaHabil(LocalDate.now(), 8);
        assertEquals(fechaEsperada, resultado.fechaMaximaDevolucion(),
                "La fecha de devolución para un empleado debe ser 8 días hábiles.");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Prueba: Invitado (Tipo 3) -> 7 días hábiles.
     * El usuario ya existe en la BD y NO tiene préstamos activos.
     */
    @Test
    void testInvitadoExistenteSieteDiasHabilesExitoso() {
        // 1. Datos
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, "3333", TIPO_INVITADO);

        // 2. Configuración de Mocks
        when(usuarioRepository.findByIdentificacion("3333")).thenReturn(Optional.of(invitadoExistente));
        when(prestamoRepository.countByUsuario_Identificacion("3333")).thenReturn(0L); // Cero préstamos activos
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo prestamo = invocation.getArgument(0);
            prestamo.setId(UUID.randomUUID());
            assertEquals(7, calcularDiasHabiles(LocalDate.now(), prestamo.getFechaMaximaDevolucion()),
                    "Debe haber 7 días hábiles entre hoy y la fecha máxima.");
            return prestamo;
        });

        // 3. Ejecución
        PrestamoResponse resultado = prestamoService.crearPrestamo(request);

        // 4. Verificación
        LocalDate fechaEsperada = calcularFechaHabil(LocalDate.now(), 7);
        assertEquals(fechaEsperada, resultado.fechaMaximaDevolucion(),
                "La fecha de devolución para un invitado debe ser 7 días hábiles.");
    }

    /**
     * Prueba: El usuario no existe y debe ser creado con el tipo especificado en el Request (e.g., Tipo 2 - Empleado).
     */
    @Test
    void testCrearPrestamoCreaUsuarioSiNoExiste() {
        // 1. Datos
        String nuevaId = "4444";
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, nuevaId, TIPO_EMPLEADO);

        // 2. Configuración de Mocks
        when(usuarioRepository.findByIdentificacion(nuevaId)).thenReturn(Optional.empty());

        // Simula la creación del usuario (se establece el ID y se retorna el objeto guardado)
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario nuevoUsuario = invocation.getArgument(0);
            nuevoUsuario.setId(UUID.randomUUID()); // Simula la ID generada por JPA
            assertEquals(TIPO_EMPLEADO, nuevoUsuario.getTipoUsuario(), "El tipo de usuario creado debe ser Empleado (2).");
            return nuevoUsuario;
        });

        // Simula el guardado del préstamo
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(invocation -> {
            Prestamo prestamo = invocation.getArgument(0);
            prestamo.setId(UUID.randomUUID());
            // Verifica que el cálculo de días se base en el tipo 2 (Empleado: 8 días)
            assertEquals(8, calcularDiasHabiles(LocalDate.now(), prestamo.getFechaMaximaDevolucion()),
                    "El usuario recién creado (Empleado) debe tener 8 días hábiles.");
            return prestamo;
        });

        // 3. Ejecución
        PrestamoResponse resultado = prestamoService.crearPrestamo(request);

        // 4. Verificación
        LocalDate fechaEsperada = calcularFechaHabil(LocalDate.now(), 8);
        assertNotNull(resultado);
        assertEquals(fechaEsperada, resultado.fechaMaximaDevolucion(),
                "La fecha de devolución para un usuario recién creado (Empleado) debe ser 8 días hábiles.");
    }

    /**
     Un invitado existente (Tipo 3) ya tiene un préstamo activo. Debe lanzar excepción 400.
     */
    @Test
    void testInvitadoFallaPorLimiteDeUnPrestamo() {
        // 1. Datos
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, "3333", TIPO_INVITADO);

        // 2. Configuración de Mocks
        when(usuarioRepository.findByIdentificacion("3333")).thenReturn(Optional.of(invitadoExistente));
        when(prestamoRepository.countByUsuario_Identificacion("3333")).thenReturn(1L); // Simula 1 préstamo activo

        // 3. Ejecución y Verificación de Excepción
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            prestamoService.crearPrestamo(request);
        });

        // 4. Verifica el estado y el mensaje
        assertEquals("400 BAD_REQUEST", exception.getStatusCode().toString());
        assertTrue(exception.getReason().contains("El usuario ya tiene un libro prestado"));

        // El repositorio de préstamos NO debe ser llamado para guardar
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }


    /**
     * Prueba: Usuario con tipo no válido (ej. 99) debe lanzar excepción 400 antes de interactuar con BD.
     */
    @Test
    void testTipoUsuarioInvalidoLanzaExcepcion() {
        // 1. Datos
        String idUsuario = "5555";
        int tipoInvalido = 99;
        PrestamoRequest request = new PrestamoRequest(ISBN_TEST, idUsuario, tipoInvalido);

        // 1.1. Simular un usuario EXISTENTE con el tipo inválido (para forzar la validación de tipoUsuario)
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setId(UUID.randomUUID());
        usuarioInvalido.setIdentificacion(idUsuario);
        usuarioInvalido.setTipoUsuario(tipoInvalido); // Usamos el tipo inválido del request


        // 2. Configuración de Mocks
        // Forzamos a que encuentre el usuario para que la lógica use directamente el tipoUsuario (99)
        when(usuarioRepository.findByIdentificacion(idUsuario)).thenReturn(Optional.of(usuarioInvalido));

        // 3. Ejecución y Verificación de Excepción
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            prestamoService.crearPrestamo(request);
        });

        // 4. Verifica el estado y el mensaje
        assertEquals("400 BAD_REQUEST", exception.getStatusCode().toString());

        String expectedReason = "Tipo de usuario no permitido: " + tipoInvalido;
        assertEquals(expectedReason, exception.getReason(), "El mensaje de la excepción debe ser exacto.");

        // No debe haber intentos de guardar un préstamo
        verify(prestamoRepository, never()).save(any(Prestamo.class));
    }

    /**
     * Prueba: Consultar un préstamo existente por ID.
     */
    @Test
    void testObtenerPrestamoExitoso() {
        // 1. Datos
        UUID idPrestamo = UUID.randomUUID();
        LocalDate fechaDevolucion = LocalDate.now().plusDays(5);

        Prestamo prestamo = new Prestamo(idPrestamo, ISBN_TEST, fechaDevolucion, afiliadoExistente);

        // 2. Configuración de Mocks
        when(prestamoRepository.findById(idPrestamo)).thenReturn(Optional.of(prestamo));

        // 3. Ejecución
        var resultado = prestamoService.obtenerPrestamo(idPrestamo);

        // 4. Verificación
        assertNotNull(resultado);
        assertEquals(idPrestamo, resultado.id());
        assertEquals(ISBN_TEST, resultado.isbn());
        assertEquals(afiliadoExistente.getIdentificacion(), resultado.identificacionUsuario());
        assertEquals(afiliadoExistente.getTipoUsuario(), resultado.tipoUsuario());
        assertEquals(fechaDevolucion, resultado.fechaMaximaDevolucion());
    }

    /**
     * Prueba: Consultar un préstamo por ID inexistente debe lanzar excepción 404.
     */
    @Test
    void testObtenerPrestamoFallaSiNoExiste() {
        // 1. Datos
        UUID idInexistente = UUID.randomUUID();

        // 2. Configuración de Mocks
        when(prestamoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // 3. Ejecución y Verificación
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            prestamoService.obtenerPrestamo(idInexistente);
        });

        // 4. Verifica el estado y el mensaje
        assertEquals("404 NOT_FOUND", exception.getStatusCode().toString());
        assertTrue(exception.getReason().contains("El préstamo no existe"));
    }

    
    /**
     * Calcula la fecha que resulta de sumar días hábiles a una fecha de inicio.
     */
    private LocalDate calcularFechaHabil(LocalDate inicio, int diasHabiles) {
        LocalDate fechaResultado = inicio;
        for (int i = 0; i < diasHabiles; i++) {
            fechaResultado = fechaResultado.plusDays(1);
            while (fechaResultado.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    fechaResultado.getDayOfWeek() == DayOfWeek.SUNDAY) {
                fechaResultado = fechaResultado.plusDays(1);
            }
        }
        return fechaResultado;
    }

    /**
     * Cuenta el número de días hábiles entre una fecha de inicio y una fecha fin (exclusiva).
     */
    private long calcularDiasHabiles(LocalDate inicio, LocalDate fin) {
        long diasHabiles = 0;
        // Comenzamos a contar desde el día siguiente al inicio
        LocalDate fecha = inicio.plusDays(1);
        // Iteramos hasta el día de la devolución (inclusive)
        while (!fecha.isAfter(fin)) {
            DayOfWeek dia = fecha.getDayOfWeek();
            if (dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY) {
                diasHabiles++;
            }
            fecha = fecha.plusDays(1);
        }
        return diasHabiles;
    }
}