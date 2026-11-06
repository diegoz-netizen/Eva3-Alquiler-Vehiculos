package pe.edu.tecsup.alquiler.exception;

import java.time.LocalDateTime;
import java.util.Map;

// Formato único de error que devuelve la API: así el cliente siempre sabe qué leer
public record ErrorResponse(
        LocalDateTime fecha,
        int estado,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> campos
) {
}
