package pe.edu.tecsup.alquiler.dto;

// Lo que este servicio lee de vehiculo-service
public record VehiculoDTO(
        Long id,
        String placa,
        String estado
) {
}
