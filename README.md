# 📚 API REST: Gestión de Préstamos de Libros (`prestamosLibros`)

API desarrollada en **Spring Boot 3.x** y **Java 17** para gestionar el ciclo de vida de los préstamos de una biblioteca.

---

## 🔗 1. Documentación de la API (Swagger UI)

La documentación interactiva de todos los *endpoints* está disponible a través de Swagger UI, donde puedes probar las peticiones directamente.

| Entorno | URL de Acceso |
| :--- | :--- |
| **Local (Desarrollo)** | `http://localhost:8080/swagger-ui/index.html` |

---

## 🚦 2. Endpoints Principales

Todos los *endpoints* están prefijados con `/api/v1/prestamos`.

### POST /api/v1/prestamos

| Propósito | Crea un nuevo registro de préstamo. |
| :--- | :--- |
| **Status Éxito** | `201 Created` |
| **Cuerpo Petición** | `PrestamoRequest` (JSON con ISBN, identificaciónUsuario, tipoUsuario) |
| **Cuerpo Respuesta** | `PrestamoResponse` (ID y fecha máxima de devolución) |

### GET /api/v1/prestamos/{idPrestamo}

| Propósito | Consulta los detalles de un préstamo por su identificador único (UUID). |
| :--- | :--- |
| **Status Éxito** | `200 OK` |
| **Cuerpo Respuesta** | `PrestamoDetailResponse` (Detalles completos del préstamo) |

---

## 💻 3. Procesos Clave y Patrones Implementados

### 3.1. DTOs y Registros (Records)

Se utilizan *Records* de Java 17 para definir los DTOs de forma inmutable, concisa y segura.

* **`PrestamoRequest`**: DTO de entrada para la creación del préstamo, incluye anotaciones de validación (`@NotBlank`, `@NotNull`, `@Min`).
* **`PrestamoResponse`**: DTO de salida para respuestas `201 Created`, contiene el ID y la fecha máxima de devolución.
* **`PrestamoDetailResponse`**: DTO de salida para consultas detalladas (`GET`).

### 3.2. Validación y Manejo de Excepciones

Se implementó una estrategia robusta para manejar errores y devolver respuestas consistentes.

* **`@ControllerAdvice`**: Se utiliza una clase (`RestControllerAdvice`) para centralizar el manejo de todas las excepciones.
* **`ResponseError`**: DTO estándar para respuestas de error (contiene `status`, `message`, `date`).
* **Manejo de 400 Bad Request:**
    * Captura `ConstraintViolationException` (errores de validación) y `MethodArgumentTypeMismatchException` (tipos de datos incorrectos en la URL o Query Params).
* **Manejo de 404 Not Found:**
    * Implementación de la excepción personalizada `ResourceNotFoundException` para indicar que un recurso buscado no existe.
* **Manejo de 500 Internal Server Error:**
    * El manejador general (`@ExceptionHandler({Exception.class})`) captura errores no controlados.

### 3.3. Documentación OpenAPI (Swagger)

Se utilizaron anotaciones de **Jakarta Bean Validation** (`@NotNull`, `@NotBlank`) junto con las anotaciones **`@Schema`** de Swagger/OpenAPI en todos los DTOs y controladores. Esto asegura que la documentación en el Swagger UI:
* Muestre las **restricciones** de longitud y obligatoriedad.
* Defina claramente los **modelos de petición y respuesta** para cada *endpoint*.

### 3.4. Patrón de Repositorio Personalizado

Se utilizaron métodos de repositorio personalizados de Spring Data JPA para consultas específicas y optimizadas:
* `countByUsuario_Identificacion(String identificacion)`: Contar préstamos activos de un usuario mediante una consulta de propiedad anidada (`usuario` -> `identificacion`).
* `Optional<Prestamo> findByIdentificacionUsuario(String identificacionUsuario)`: Búsqueda de un préstamo por la identificación de un usuario.