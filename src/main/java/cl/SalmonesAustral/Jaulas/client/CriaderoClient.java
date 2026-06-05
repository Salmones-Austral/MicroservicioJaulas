package cl.SalmonesAustral.Jaulas.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CriaderoClient {
    private final WebClient webClient;

    public CriaderoClient(WebClient.Builder builder) {
        // Apunta al MS de Criaderos
        this.webClient = builder.baseUrl("http://localhost:8080/api/v1/criaderos").build();
    }

    public boolean existeCriadero(Long criaderoId) {
        try {
            webClient.get()
                    .uri("/{id}", criaderoId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true; // Si responde 200 OK, existe
        } catch (WebClientResponseException.NotFound e) {
            return false; // Si responde 404, no existe
        } catch (Exception e) {
            System.err.println("Error de conexión con Criadero: " + e.getMessage());
            return false;
        }
    }
}
