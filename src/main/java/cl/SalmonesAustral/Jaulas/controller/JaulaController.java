package cl.SalmonesAustral.Jaulas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import cl.SalmonesAustral.Jaulas.service.JaulaService;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/api/v1/jaulas")
public class JaulaController {

    private final JaulaService jaulaService;
     private  WebClient webClient;

    // Constructor (igual que tu ejemplo)
    public JaulaController(JaulaService jaulaService) {
        this.jaulaService = jaulaService;
    }

    // Obtener todas las jaulas
    @GetMapping
    public ResponseEntity<List<Jaulas>> listarJaulas() {
        List<Jaulas> jaulas = jaulaService.getJaulas();
        return ResponseEntity.ok(jaulas);
    }

    // Crear jaula
    @PostMapping
    public ResponseEntity<Jaulas> agregarJaula(@RequestBody Jaulas jaula) {
        Jaulas nueva = jaulaService.saveJaula(jaula);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Jaulas> buscarJaula(@PathVariable int id) {
        Jaulas jaula = jaulaService.getJaulaById((long) id);
        if (jaula == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jaula);
    }

    // Actualizar
    @PutMapping("/{id}")

    public ResponseEntity<Jaulas> actualizarJaula(
            @PathVariable int id,
            @RequestBody Jaulas jaula) {

        jaula.setId((long) id); // importante: el id viene del path
        Jaulas actualizada = jaulaService.updateJaula(jaula);

        if (actualizada == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(actualizada);
    }
        

    //  Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarJaula(@PathVariable int id) 
    {
        jaulaService.deleteJaula((long) id);
        return ResponseEntity.ok("Jaula eliminada");
        

    }

    // Total de jaulas
    @GetMapping("/total")
    public ResponseEntity<Integer> totalJaulas() {
        int total = jaulaService.totalJaulas();
        return ResponseEntity.ok(total);
    }

    // Buscar por criaderoId (lógica de negocio clave)
    @GetMapping("/criadero/{criaderoId}")
    public ResponseEntity<List<Jaulas>> obtenerPorCriadero(@PathVariable long criaderoId) {
        List<Jaulas> lista = jaulaService.obtenerPorCriadero(criaderoId);
        return ResponseEntity.ok(lista);
    }

    // 🔗 Enviar mensaje a Criadero
    @GetMapping("/notificar-criadero")
    public ResponseEntity<String> notificarCriadero(@RequestParam String mensaje) {

    String respuesta = webClient.get()
            .uri("http://localhost:8080/api/criaderos/recibir?mensaje={mensaje}", mensaje)
            .retrieve()
            .bodyToMono(String.class)
            .block();

    return ResponseEntity.ok(respuesta);
}

// 📩 Recibir mensaje desde Jaula
    @GetMapping("/recibir")
    public ResponseEntity<String> recibirMensaje(@RequestParam String mensaje) {

    System.out.println("📩 Mensaje recibido desde Jaulas: " + mensaje);

    return ResponseEntity.ok("Criadero recibió: " + mensaje);
}
}