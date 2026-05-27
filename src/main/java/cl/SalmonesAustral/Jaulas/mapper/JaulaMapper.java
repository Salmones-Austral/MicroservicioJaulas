package cl.SalmonesAustral.Jaulas.mapper;

import cl.SalmonesAustral.Jaulas.dto.CreateJaulaRequest;
import cl.SalmonesAustral.Jaulas.dto.UpdateJaulaRequest;
import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class JaulaMapper {

    /**
     * Convierte DTO de creación a modelo Jaula (POST)
     * El ID se manda como 0 porque lo genera la base de datos
     */

    public static Jaulas toModel(CreateJaulaRequest request) {
        return new Jaulas(
                null, // ID temporal, se genera en BD
                request.codigo(),
                request.capacidadMaxima(),
                request.cantidadActual(),
                request.activa(),
                request.criaderoId()
        );
    }
     /*@NotBlank(message = "Código no puede ser vacío") String codigo,
                @PositiveOrZero(message = "Capacidad máxima no puede ser negativa") int capacidadMaxima,
                @PositiveOrZero(message = "Cantidad actual no puede ser negativa") int cantidadActual,
                @NotBlank(message = "Activa no puede ser vacía") Boolean activa,
                @NotBlank(message = "Criadero ID no puede ser vacío") Long criaderoId) {
*/
    /**
     * Convierte DTO de actualización a modelo Jaula (PUT)
     * El ID viene desde el path (controller)
     */
    public static Jaulas toModel(Long id, UpdateJaulaRequest request) {
        return new Jaulas(
                id, // ID real
                request.codigo(),
                request.capacidadMaxima(),
                request.cantidadActual(),
                request.activa(),
                request.criaderoId()
        );
    }
}


