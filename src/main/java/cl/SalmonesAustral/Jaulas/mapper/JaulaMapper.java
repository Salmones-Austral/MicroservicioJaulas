package cl.SalmonesAustral.Jaulas.mapper;

import cl.SalmonesAustral.Jaulas.dto.CreateJaulaRequest;
import cl.SalmonesAustral.Jaulas.dto.UpdateJaulaRequest;
import cl.SalmonesAustral.Jaulas.modelo.Jaulas;

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
                request.criaderoId(),
                request.habilitarAlimentacion()
        );
    }

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
                request.criaderoId(),
                request.habilitarAlimentacion()
        );
    }
}


