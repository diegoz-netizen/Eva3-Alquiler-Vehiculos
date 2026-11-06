package pe.edu.tecsup.alquiler.dto;

import jakarta.validation.constraints.NotNull;
import pe.edu.tecsup.alquiler.model.EstadoAlquiler;

// Cuerpo del PATCH /api/alquileres/{id}/estado, por ejemplo {"estado": "FINALIZADO"}
public record EstadoRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoAlquiler estado
) {
}
