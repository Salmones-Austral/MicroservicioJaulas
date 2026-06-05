package cl.SalmonesAustral.Jaulas.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "jaulas") // <-- AGREGA ESTO
public class Jaulas {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private int capacidadMaxima;

    private int cantidadActual;

    private Boolean activa;

    //En vez de relación, solo guardas el ID
    private Long criaderoId;

    // Constructores
    public Jaulas() {
    }

    public Jaulas(Long id, String codigo, int capacidadMaxima, int cantidadActual, Boolean activa, Long criaderoId) {
        this.id = id;
        this.codigo = codigo;
        this.capacidadMaxima = capacidadMaxima;
        this.cantidadActual = cantidadActual;
        this.activa = activa;
        this.criaderoId = criaderoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(int cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Long getCriaderoId() {
        return criaderoId;
    }

    public void setCriaderoId(Long criaderoId) {
        this.criaderoId = criaderoId;
    }
}