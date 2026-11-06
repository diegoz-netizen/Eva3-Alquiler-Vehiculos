package pe.edu.tecsup.cliente.exception;

// Se lanza cuando los datos tienen buen formato pero rompen una regla (por ejemplo, un DNI repetido): 409
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
