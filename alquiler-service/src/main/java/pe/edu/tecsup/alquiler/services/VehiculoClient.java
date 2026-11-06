package pe.edu.tecsup.alquiler.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import pe.edu.tecsup.alquiler.dto.VehiculoDTO;
import pe.edu.tecsup.alquiler.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.alquiler.exception.ServicioNoDisponibleException;

import java.time.Duration;

// Consulta a vehiculo-service por HTTP
@Component
public class VehiculoClient {

    private final RestClient restClient;

    public VehiculoClient(@Value("${servicios.vehiculo.url}") String urlBase) {
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(2));
        fabrica.setReadTimeout(Duration.ofSeconds(3));
        this.restClient = RestClient.builder()
                .baseUrl(urlBase)
                .requestFactory(fabrica)
                .build();
    }

    public VehiculoDTO obtener(Long idVehiculo) {
        try {
            return restClient.get()
                    .uri("/api/vehiculos/{id}", idVehiculo)
                    .retrieve()
                    .body(VehiculoDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new RecursoNoEncontradoException("No existe un vehículo con id " + idVehiculo);
        } catch (ResourceAccessException ex) {
            throw new ServicioNoDisponibleException("vehiculo-service no está disponible", ex);
        }
    }
}
