package pe.edu.tecsup.alquiler.dto;

import pe.edu.tecsup.alquiler.model.Alquiler;
import pe.edu.tecsup.alquiler.model.EstadoAlquiler;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlquilerResponse(
        Long id,
        Long idCliente,
        Long idVehiculo,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal tarifaDiaria,
        Integer dias,
        BigDecimal importeEstimado,
        EstadoAlquiler estado,
        LocalDateTime fechaRegistro
) {

    public static AlquilerResponse desde(Alquiler alquiler) {
        return new AlquilerResponse(
                alquiler.getId(),
                alquiler.getIdCliente(),
                alquiler.getIdVehiculo(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getTarifaDiaria(),
                alquiler.getDias(),
                alquiler.getImporteEstimado(),
                alquiler.getEstado(),
                alquiler.getFechaRegistro()
        );
    }
}
