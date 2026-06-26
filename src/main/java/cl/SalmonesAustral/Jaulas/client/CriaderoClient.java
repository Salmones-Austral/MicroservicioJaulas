package cl.SalmonesAustral.Jaulas.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class CriaderoClient {
    private final WebClient webClient;

    // Usamos @Value para leer la ruta desde la configuración
    public CriaderoClient(WebClient.Builder builder, @Value("${criaderos.service.url}") String criaderosUrl) {
        this.webClient = builder.baseUrl(criaderosUrl).build();
    }

    public boolean existeCriadero(Long criaderoId) {
        try {
            System.out.println("Buscando criadero ID " + criaderoId + " en la URL configurada...");
            
            webClient.get()
                    .uri("/{id}", criaderoId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
                    
            System.out.println("¡Criadero " + criaderoId + " encontrado!");
            return true;
            
        } catch (WebClientResponseException.NotFound e) {
            System.out.println("El criadero " + criaderoId + " no existe en la BD (Error 404).");
            return false;
            
        } catch (Exception e) {
            System.err.println("🔴 ERROR CRÍTICO DE CONEXIÓN CON MICROSERVICIO CRIADERO 🔴");
            System.err.println("Detalle del error: " + e.getMessage());
            return false;
        }
    }
}
