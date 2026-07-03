package cl.SalmonesAustral.Jaulas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.Valid;

import cl.SalmonesAustral.Jaulas.dto.CreateJaulaRequest;
import cl.SalmonesAustral.Jaulas.dto.UpdateJaulaRequest;
import cl.SalmonesAustral.Jaulas.mapper.JaulaMapper;
import cl.SalmonesAustral.Jaulas.modelo.Jaulas;
import cl.SalmonesAustral.Jaulas.service.JaulaService;

@RestController                                                                  // Define que esta clase expone endpoints de una API RESTful
@RequestMapping("/api/v1/jaulas")                                                // Establece la ruta base URL de acceso para este controlador
public class JaulaController {

    private final JaulaService jaulaService;                                     // Variable para la lógica de negocio de las jaulas
    private final WebClient webClient;                                           // Variable para las peticiones HTTP externas

    public JaulaController(JaulaService jaulaService, WebClient.Builder webClientBuilder) { // Constructor que inyecta las dependencias necesarias
        this.jaulaService = jaulaService;                                        // Asigna el servicio de base de datos inyectado
        this.webClient = webClientBuilder.build();                               // Construye la instancia lista del cliente WebClient
    }

    @GetMapping                                                                  // Responde a peticiones GET en la ruta raíz (/api/v1/jaulas)
    public ResponseEntity<List<Jaulas>> listarJaulas() {                         // Método para retornar todas las jaulas registradas
        return ResponseEntity.ok(jaulaService.getJaulas());                      // Llama al servicio, obtiene la lista y responde con HTTP 200 OK
    }

    //CAMBIO AQUÍ: Eliminado el BindingResult
    @PostMapping                                                                 // Responde a peticiones POST para inserciones de datos
    public ResponseEntity<?> agregarJaula(@Valid @RequestBody CreateJaulaRequest request) { // Recibe y valida el JSON entrante contra el DTO

        // Usamos el Mapper directo. Si el @Valid falla, el GlobalExceptionHandler lo atrapa.
        Jaulas jaula = JaulaMapper.toModel(request);                             // Pasa los datos limpios del DTO a la entidad del modelo
        Jaulas nueva = jaulaService.saveJaula(jaula);                            // Almacena el nuevo registro en la base de datos

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);            // Retorna la jaula creada con el estado HTTP 201 Created
    }

    @GetMapping("/{id}")                                                         // Responde a peticiones GET filtrando por ID en la URL
    public ResponseEntity<Jaulas> buscarJaula(@PathVariable Long id) {           // Captura la variable ID directamente de la ruta
        return ResponseEntity.ok(jaulaService.getJaulaById(id));                 // Busca la jaula y la retorna con un HTTP 200 OK
    }

    //CAMBIO AQUÍ: Eliminado el BindingResult
    @PutMapping("/{id}")                                                         // Responde a peticiones PUT para actualizar registros por ID
    public ResponseEntity<?> actualizarJaula(
            @PathVariable Long id,                                               // Captura el identificador de la jaula a modificar
            @Valid @RequestBody UpdateJaulaRequest request) {                    // Valida estructuralmente el JSON de actualización

        // Usamos el Mapper directo
        Jaulas jaula = JaulaMapper.toModel(id, request);                         // Convierte el DTO e integra el ID correspondiente
        Jaulas actualizada = jaulaService.updateJaula(id, jaula);                // Modifica el registro en la persistencia de datos

        return ResponseEntity.ok(actualizada);                                   // Retorna los datos actualizados con HTTP 200 OK
    }

    @DeleteMapping("/{id}")                                                      // Responde a peticiones DELETE apuntando a un ID específico
    public ResponseEntity<String> eliminarJaula(@PathVariable Long id) {         // Captura el ID de la jaula a remover
        jaulaService.deleteJaula(id);                                            // Ordena al servicio borrar la jaula de la BD
        return ResponseEntity.ok("Jaula eliminada");                             // Responde con un texto de confirmación y HTTP 200 OK
    }

    @GetMapping("/total")                                                        // Responde a peticiones GET en la ruta "/total"
    public ResponseEntity<Integer> totalJaulas() {                               // Método enfocado en reportería rápida de conteos
        return ResponseEntity.ok(jaulaService.totalJaulas());                    // Cuenta los registros y devuelve el número entero con HTTP 200
    }

    @GetMapping("/criadero/{criaderoId}")                                        // Responde a GET mapeando las jaulas de un criadero
    public ResponseEntity<List<Jaulas>> obtenerPorCriadero(@PathVariable Long criaderoId) { // Recibe el ID del criadero dueño
        return ResponseEntity.ok(jaulaService.obtenerPorCriadero(criaderoId));   // Retorna la sublista filtrada con HTTP 200 OK
    }

    // ==========================================
    // COMUNICACIONES (Llamadas de Red)
    // ==========================================
    
    @GetMapping("/notificar-criadero")                                           // Responde a GET en la ruta "/notificar-criadero"
    public ResponseEntity<String> notificarCriadero(@RequestParam String mensaje) { // Captura el texto paramétrico desde la URL
        String respuesta = webClient.get()                                       // Inicializa el cliente HTTP para disparar un GET
                .uri("http://localhost:8080/api/v1/criaderos/recibir?mensaje={mensaje}", mensaje) // Apunta al endpoint de escucha de Criaderos
                .retrieve()                                                      // Ejecuta la petición hacia el puerto 8080
                .bodyToMono(String.class)                                        // Espera capturar una respuesta en formato String
                .block();                                                        // Comunicación Síncrona: Frena hasta que Criaderos conteste

        return ResponseEntity.ok(respuesta);                                     // Entrega al usuario final el mensaje que retornó el otro MS
    }

    @GetMapping("/recibir")                                                      // Endpoint buzón en la ruta "/recibir"
    public ResponseEntity<String> recibirMensaje(@RequestParam String mensaje) { // Lee el parámetro enviado por el microservicio externo
        System.out.println("📩 Mensaje recibido desde Jaulas: " + mensaje);       // Muestra el texto en los logs internos de la consola
        return ResponseEntity.ok("Jaula recibió: " + mensaje);                    // Retorna un acuse de recibo string con HTTP 200 OK
    }

    @GetMapping("/criadero/{id}/activas")                                        // Responde a GET para verificar operatividad en cascada
    public ResponseEntity<Boolean> tieneJaulasActivas(@PathVariable Long id) {    // Captura el ID del criadero a auditar
        boolean tieneActivas = jaulaService.tieneJaulasActivas(id);              // Ejecuta la lógica booleana de búsqueda en el servicio
        return ResponseEntity.ok(tieneActivas);                                  // Retorna true o false envuelto en un HTTP 200 OK
    }
    //entregar a MS alimentacion
    @GetMapping("/codigos")
    public ResponseEntity<List<String>> listarTodosLosCodigos() {
        List<String> codigos = jaulaService.obtenerTodosLosCodigos(); 
        return ResponseEntity.ok(codigos);
    }
}