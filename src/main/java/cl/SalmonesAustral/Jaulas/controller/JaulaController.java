package cl.SalmonesAustral.Jaulas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.Valid;

import cl.SalmonesAustral.Jaulas.dto.CreateJaulaRequest;
import cl.SalmonesAustral.Jaulas.dto.UpdateJaulaRequest;
import cl.SalmonesAustral.Jaulas.mapper.JaulaMapper;
import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import cl.SalmonesAustral.Jaulas.service.JaulaService;

@RestController
@RequestMapping("/api/v1/jaulas")
public class JaulaController {

    private final JaulaService jaulaService;
    private final WebClient webClient;

    public JaulaController(JaulaService jaulaService, WebClient.Builder webClientBuilder) {
        this.jaulaService = jaulaService;
        this.webClient = webClientBuilder.build();
    }

    @GetMapping
    public ResponseEntity<List<Jaulas>> listarJaulas() {
        return ResponseEntity.ok(jaulaService.getJaulas());
    }

    @PostMapping
    public ResponseEntity<?> agregarJaula(@Valid @RequestBody CreateJaulaRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        // Usamos el Mapper
        Jaulas jaula = JaulaMapper.toModel(request);
        Jaulas nueva = jaulaService.saveJaula(jaula);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jaulas> buscarJaula(@PathVariable Long id) {
        return ResponseEntity.ok(jaulaService.getJaulaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarJaula(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJaulaRequest request, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        Jaulas jaula = JaulaMapper.toModel(id, request);
        Jaulas actualizada = jaulaService.updateJaula(id, jaula);

        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarJaula(@PathVariable Long id) {
        jaulaService.deleteJaula(id);
        return ResponseEntity.ok("Jaula eliminada");
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> totalJaulas() {
        return ResponseEntity.ok(jaulaService.totalJaulas());
    }

    @GetMapping("/criadero/{criaderoId}")
    public ResponseEntity<List<Jaulas>> obtenerPorCriadero(@PathVariable Long criaderoId) {
        return ResponseEntity.ok(jaulaService.obtenerPorCriadero(criaderoId));
    }

    // COMUNICACIONES
    @GetMapping("/notificar-criadero")
    public ResponseEntity<String> notificarCriadero(@RequestParam String mensaje) {
        String respuesta = webClient.get()
                .uri("http://localhost:8080/api/v1/criaderos/recibir?mensaje={mensaje}", mensaje)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/recibir")
    public ResponseEntity<String> recibirMensaje(@RequestParam String mensaje) {
        System.out.println("📩 Mensaje recibido desde Jaulas: " + mensaje);
        return ResponseEntity.ok("Jaula recibió: " + mensaje);
    }
}