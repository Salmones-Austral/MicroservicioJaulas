package cl.SalmonesAustral.Jaulas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.SalmonesAustral.Jaulas.modelo.Jaulas;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface JaulaRepository extends JpaRepository<Jaulas, Long> {

    //TOTAL (igual que tu ejemplo)

    default int totalJaulas() {
        return (int) this.count();
    }

  // Buscar jaulas por criadero
    @Query(value = "SELECT * FROM jaulas WHERE criadero_id = :criaderoId", nativeQuery = true)
    List<Jaulas> findByCriaderoId(@Param("criaderoId") Long criaderoId);

    // Buscar jaulas activas
    @Query(value = "SELECT * FROM jaulas WHERE activa = true", nativeQuery = true)
    List<Jaulas> findActivas();

    @Query("SELECT DISTINCT j.codigo FROM Jaulas j")
    List<String> obtenerTodosLosCodigos();
}
