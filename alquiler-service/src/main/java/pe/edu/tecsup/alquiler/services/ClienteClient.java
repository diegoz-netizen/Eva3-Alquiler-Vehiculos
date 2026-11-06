package pe.edu.tecsup.alquiler.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import pe.edu.tecsup.alquiler.dto.ClienteDTO;
import pe.edu.tecsup.alquiler.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.alquiler.exception.ServicioNoDisponibleException;

import java.time.Duration;

// Consulta a cliente-service por HTTP. Así se respeta que cada servicio sea dueño de sus datos:
// alquiler-service no lee la tabla de clientes, le pregunta al servicio.
@Component
public class ClienteClient {

    private final RestClient restClient;

    public ClienteClient(@Value("${servicios.cliente.url}") String urlBase) {
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(2));
        fabrica.setReadTimeout(Duration.ofSeconds(3));
        this.restClient = RestClient.builder()
                .baseUrl(urlBase)
                .requestFactory(fabrica)
                .build();
    }

    public ClienteDTO obtener(Long idCliente) {
        try {
            return restClient.get()
                    .uri("/api/clientes/{id}", idCliente)
                    .retrieve()
                    .body(ClienteDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RecursoNoEncontradoException("No existe un cliente con id " + idCliente);
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException("cliente-service no está disponible", ex);
        }
    }
}
