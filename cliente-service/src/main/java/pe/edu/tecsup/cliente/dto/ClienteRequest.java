package pe.edu.tecsup.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Datos que llegan en el POST y el PUT. Las anotaciones validan antes de tocar la base de datos.
public record ClienteRequest(

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
        String dni,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 80, message = "Los nombres no pueden pasar de 80 caracteres")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(max = 80, message = "Los apellidos no pueden pasar de 80 caracteres")
        String apellidos,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        @Size(max = 120, message = "El correo no puede pasar de 120 caracteres")
        String correo,

        @Pattern(regexp = "9\\d{8}", message = "El teléfono debe tener 9 dígitos y empezar con 9")
        String telefono,

        @NotBlank(message = "El número de licencia es obligatorio")
        @Pattern(regexp = "[A-Z]\\d{8}", message = "La licencia debe ser una letra mayúscula seguida de 8 dígitos (ej. Q12345678)")
        String numeroLicencia
) {
}
