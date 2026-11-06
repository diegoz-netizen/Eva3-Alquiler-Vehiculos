package pe.edu.tecsup.cliente.dto;

import pe.edu.tecsup.cliente.model.Cliente;

import java.time.LocalDateTime;

// Lo que devuelve la API: no se expone la entidad directamente
public record ClienteResponse(
        Long id,
        String dni,
        String nombres,
        String apellidos,
        String correo,
        String telefono,
        String numeroLicencia,
        LocalDateTime fechaRegistro
) {

    public static ClienteResponse desde(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getCorreo(),
                cliente.getTelefono(),
                cliente.getNumeroLicencia(),
                cliente.getFechaRegistro()
        );
    }
}
