package cl.SalmonesAustral.Jaulas.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import cl.SalmonesAustral.Jaulas.repository.JaulaRepository;
import cl.SalmonesAustral.Jaulas.client.CriaderoClient;

@Service
public class JaulaService {

    private final JaulaRepository jaulaRepository;
    private final CriaderoClient criaderoClient;

    // Inyección por constructor (Best Practice)
    public JaulaService(JaulaRepository jaulaRepository, CriaderoClient criaderoClient) {
        this.jaulaRepository = jaulaRepository;
        this.criaderoClient = criaderoClient;
    }

    // se reutiliza para listar en MS alimentacion
    public List<Jaulas> getJaulas() {
        return jaulaRepository.findAll();
    }

    public Jaulas saveJaula(Jaulas jaula) {
        validarReglasDeNegocio(jaula);
        return jaulaRepository.save(jaula);
    }

    public Jaulas getJaulaById(Long id) {
        // Lanzamos RuntimeException (puedes cambiarlo por tu ResourceNotFoundException)
        return jaulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jaula no encontrada con ID: " + id));
    }

    public Jaulas updateJaula(Long id, Jaulas jaulaActualizada) {
        Jaulas existente = getJaulaById(id); // Valida que exista
        
        validarReglasDeNegocio(jaulaActualizada);

        existente.setCodigo(jaulaActualizada.getCodigo());
        existente.setCapacidadMaxima(jaulaActualizada.getCapacidadMaxima());
        existente.setCantidadActual(jaulaActualizada.getCantidadActual());
        existente.setActiva(jaulaActualizada.getActiva());
        existente.setCriaderoId(jaulaActualizada.getCriaderoId());

        return jaulaRepository.save(existente);
    }

    public List<Jaulas> obtenerPorCriadero(Long criaderoId) {
        return jaulaRepository.findByCriaderoId(criaderoId);
    }

    public void deleteJaula(Long id) {
        jaulaRepository.deleteById(id);
    }

    public int totalJaulas() {
        return (int) jaulaRepository.count();
    }

    // --- MÉTODO PRIVADO PARA REGLAS DE NEGOCIO ---
    private void validarReglasDeNegocio(Jaulas jaula) {
        if (jaula.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0.");
        }
        if (jaula.getCantidadActual() > jaula.getCapacidadMaxima()) {
            throw new IllegalArgumentException("La cantidad actual no puede superar la capacidad máxima.");
        }
        if (!criaderoClient.existeCriadero(jaula.getCriaderoId())) {
            throw new IllegalArgumentException("El Criadero con ID " + jaula.getCriaderoId() + " no existe.");
        }
    }

    // Verifica si un criadero tiene jaulas activas
    public boolean tieneJaulasActivas(Long criaderoId) {
        List<Jaulas> jaulasDelCriadero = jaulaRepository.findByCriaderoId(criaderoId);
        // Devuelve true si al menos una jaula tiene activa == true
        return jaulasDelCriadero.stream().anyMatch(Jaulas::getActiva);
    }

    /////////////////////PARA EL MS ALIMENTACION///////////////////////////////
    //para listar Codigos y estado de las jaulas
    public List<Map<String, Object>> listarEstadosAlimentacion() {
        List<Jaulas> todasLasJaulas = jaulaRepository.findAll();
        
        return todasLasJaulas.stream().map(jaula -> {
            Map<String, Object> estadoJaula = new HashMap<>();
            estadoJaula.put("codigo", jaula.getCodigo());
            estadoJaula.put("habilitarAlimentacion", jaula.getHabilitarAlimentacion());
            return estadoJaula;
        }).collect(Collectors.toList());
    }

    //validacion de si existe el codigo de la jaula
    public Jaulas buscarPorCodigo(String codigo) {
        return jaulaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Jaula no encontrada con el código: " + codigo));
    }

    //Para actualizar el estado por medio del codigo de la jaula
    public Jaulas actualizarEstadoAlimentacion(String codigo, Boolean habilitarAlimentacion) {
        Jaulas jaula = buscarPorCodigo(codigo);
        
        jaula.setHabilitarAlimentacion(habilitarAlimentacion);
        
        return jaulaRepository.save(jaula);
    }
}