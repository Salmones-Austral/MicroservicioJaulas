package cl.SalmonesAustral.Jaulas.dto;

public class JaulaResponse {
    private Long id;
    private String codigo;
    private int capacidadMaxima;
    private int cantidadActual;
    private boolean activa;
    private Long criaderoId;

    // GETTERS & SETTERS

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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Long getCriaderoId() {
        return criaderoId;
    }

    public void setCriaderoId(Long criaderoId) {
        this.criaderoId = criaderoId;
    }

}
