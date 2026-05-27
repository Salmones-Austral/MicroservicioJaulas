package cl.SalmonesAustral.Jaulas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import cl.SalmonesAustral.Jaulas.repository.JaulaRepository;

@Service
public class JaulaService {

    @Autowired
    private JaulaRepository jaulaRepository;

    //  Obtener todas
    public List<Jaulas> getJaulas() {
        return jaulaRepository.findAll();
    }

    // Guardar (CON lógica de negocio)
    public Jaulas saveJaula(Jaulas jaula) {

        //  REGLA 1: no superar capacidad
        if (jaula.getCantidadActual() > jaula.getCapacidadMaxima()) {
            return null;
        }

        //  REGLA 2: capacidad debe ser mayor a 0
        if (jaula.getCapacidadMaxima() <= 0) {
            return null;
        }

        return jaulaRepository.save(jaula);
    }

    //  Obtener por ID
    public Jaulas getJaulaById(Long id) {
        return jaulaRepository.findById(id).orElse(null);
    }

    //  Actualizar (CON lógica de negocio)
    public Jaulas updateJaula(Jaulas jaula) {

        // misma validación que en create
        if (jaula.getCantidadActual() > jaula.getCapacidadMaxima()) {
            return null;
        }

        if (jaula.getCapacidadMaxima() <= 0) {
            return null;
        }

        return jaulaRepository.save(jaula);
    }

    // Actualizar por ID (se usa desde el controlador PUT)
    public Jaulas updateJaula(Long id, Jaulas jaula) {
        Jaulas existente = getJaulaById(id);
        if (existente == null) {
            return null;
        }

        if (jaula.getCantidadActual() > jaula.getCapacidadMaxima()) {
            return null;
        }

        if (jaula.getCapacidadMaxima() <= 0) {
            return null;
        }

        existente.setCodigo(jaula.getCodigo());
        existente.setCapacidadMaxima(jaula.getCapacidadMaxima());
        existente.setCantidadActual(jaula.getCantidadActual());
        existente.setActiva(jaula.getActiva());
        existente.setCriaderoId(jaula.getCriaderoId());

        return jaulaRepository.save(existente);
    }

    // Buscar por criadero
    public List<Jaulas> obtenerPorCriadero(Long criaderoId) {
        return jaulaRepository.findByCriaderoId(criaderoId);
    }

    // Eliminar
    public String deleteJaula(Long id) {
        jaulaRepository.deleteById(id);
        return "Jaula eliminada";
    }

    // EXTRA: total
    public int totalJaulas() {
        return (int) jaulaRepository.count();
    }

}