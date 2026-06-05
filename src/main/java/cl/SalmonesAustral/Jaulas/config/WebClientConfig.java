package cl.SalmonesAustral.Jaulas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {return WebClient.builder();}
    @Bean
    public WebClient JaulaWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8080/api/criaderos") // microservicio externo
                .build();
    }
}