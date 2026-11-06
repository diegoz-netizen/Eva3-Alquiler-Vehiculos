package pe.edu.tecsup.alquiler.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// Datos que llegan para registrar o actualizar un alquiler
public record AlquilerRequest(

        @NotNull(message = "El idCliente es obligatorio")
        @Positive(message = "El idCliente debe ser un número positivo")
        Long idCliente,

        @NotNull(message = "El idVehiculo es obligatorio")
        @Positive(message = "El idVehiculo debe ser un número positivo")
        Long idVehiculo,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
        LocalDate fechaInicio,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin,

        @NotNull(message = "La tarifa diaria es obligatoria")
        @DecimalMin(value = "0.00", message = "La tarifa diaria no puede ser negativa")
        @Digits(integer = 8, fraction = 2, message = "La tarifa diaria admite hasta 8 enteros y 2 decimales")
        BigDecimal tarifaDiaria
) {

    // Validación entre dos campos: Bean Validation la ejecuta junto con las demás.
    // Si alguna fecha falta, ya la reporta @NotNull; aquí solo se compara el orden.
    @AssertTrue(message = "La fecha de fin debe ser posterior a la fecha de inicio")
    public boolean isRangoDeFechasValido() {
        return fechaInicio == null || fechaFin == null || fechaFin.isAfter(fechaInicio);
    }
}
