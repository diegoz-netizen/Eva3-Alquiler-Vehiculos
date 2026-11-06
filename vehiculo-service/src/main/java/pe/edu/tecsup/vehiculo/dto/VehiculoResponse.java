package pe.edu.tecsup.vehiculo.dto;

import pe.edu.tecsup.vehiculo.model.EstadoVehiculo;
import pe.edu.tecsup.vehiculo.model.Vehiculo;

import java.time.LocalDateTime;

public record VehiculoResponse(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer anio,
        String color,
        EstadoVehiculo estado,
        LocalDateTime fechaRegistro
) {

    public static VehiculoResponse desde(Vehiculo vehiculo) {
        return new VehiculoResponse(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getColor(),
                vehiculo.getEstado(),
                vehiculo.getFechaRegistro()
        );
    }
}
