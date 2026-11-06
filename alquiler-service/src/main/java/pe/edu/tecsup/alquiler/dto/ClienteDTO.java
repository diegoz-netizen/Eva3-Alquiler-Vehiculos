package pe.edu.tecsup.alquiler.dto;

// Lo que este servicio lee de cliente-service. Solo se declaran los campos que se usan;
// el resto del JSON se ignora.
public record ClienteDTO(
        Long id,
        String nombres,
        String apellidos
) {
}
