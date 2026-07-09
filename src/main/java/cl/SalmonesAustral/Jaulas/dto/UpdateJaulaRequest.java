package cl.SalmonesAustral.Jaulas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateJaulaRequest(
        @NotBlank(message = "Código no puede ser vacío") String codigo,
        @PositiveOrZero(message = "Capacidad máxima no puede ser negativa") int capacidadMaxima,
        @PositiveOrZero(message = "Cantidad actual no puede ser negativa") int cantidadActual,      
        @NotNull(message = "Activa no puede ser nula") Boolean activa,
        @NotNull(message = "Criadero ID no puede ser nulo") Long criaderoId,
        @NotNull(message = "Habilitar alimentación no puede ser nulo") Boolean habilitarAlimentacion
) {}