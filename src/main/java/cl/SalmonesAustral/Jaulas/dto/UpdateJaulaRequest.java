package cl.SalmonesAustral.Jaulas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para actualizar un libro existente (PUT) No incluye ID porque se obtiene del path parameter
 */
public record UpdateJaulaRequest
                (@NotBlank(message = "Código no puede ser vacío") String codigo,
                @PositiveOrZero(message = "Capacidad máxima no puede ser negativa") int capacidadMaxima,
                @PositiveOrZero(message = "Cantidad actual no puede ser negativa") int cantidadActual,      
                @NotBlank(message = "Activa no puede ser vacía") Boolean activa,
                @NotBlank(message = "Criadero ID no puede ser vacío") Long criaderoId) {
                }
