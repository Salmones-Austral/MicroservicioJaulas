package cl.SalmonesAustral.Jaulas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CriaderoClient {
    private final WebClient webClient;

    public CriaderoClient(WebClient.Builder builder) {
        // Asegúrate de que esta URL sea EXACTAMENTE la misma que usas en Postman
        this.webClient = builder.baseUrl("http://localhost:8080/api/v1/criaderos").build();
    }

    public boolean existeCriadero(Long criaderoId) {
        try {
            System.out.println("Buscando criadero ID " + criaderoId + " en puerto 8080...");
            
            // Hacemos el GET tal cual lo harías en Postman
            webClient.get()
                    .uri("/{id}", criaderoId)
                    .retrieve()
                    .bodyToMono(Object.class) // Mejor que toBodilessEntity para evitar errores de Jackson
                    .block();
                    
            System.out.println("¡Criadero " + criaderoId + " encontrado!");
            return true; // Si responde 200 OK
            
        } catch (WebClientResponseException.NotFound e) {
            System.out.println("El criadero " + criaderoId + " no existe en la BD (Error 404).");
            return false;
            
        } catch (Exception e) {
            // Si el problema es de conexión (ej. puerto apagado o mala URL), saldrá por aquí
            System.err.println("🔴 ERROR CRÍTICO DE CONEXIÓN CON MICROSERVICIO CRIADERO 🔴");
            System.err.println("Detalle del error: " + e.getMessage());
            return false;
        }
    }
}
