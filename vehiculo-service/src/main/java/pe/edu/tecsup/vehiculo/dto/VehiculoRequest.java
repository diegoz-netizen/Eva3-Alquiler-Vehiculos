package pe.edu.tecsup.vehiculo.dto;

import jakarta.validation.constraints.*;
import pe.edu.tecsup.vehiculo.model.EstadoVehiculo;

// Datos que llegan en el POST y el PUT
public record VehiculoRequest(

        @NotBlank(message = "La placa es obligatoria")
        @Pattern(regexp = "[A-Z0-9]{3}-\\d{3}", message = "La placa debe tener el formato ABC-123")
        String placa,

        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 40, message = "La marca no puede pasar de 40 caracteres")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 40, message = "El modelo no puede pasar de 40 caracteres")
        String modelo,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 1990, message = "El año no puede ser menor a 1990")
        @Max(value = 2030, message = "El año no puede ser mayor a 2030")
        Integer anio,

        @Size(max = 30, message = "El color no puede pasar de 30 caracteres")
        String color,

        // Opcional: si no se envía, el vehículo queda DISPONIBLE
        EstadoVehiculo estado
) {
}
