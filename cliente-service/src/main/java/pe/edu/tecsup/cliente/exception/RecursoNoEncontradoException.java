package pe.edu.tecsup.cliente.exception;

// Se lanza cuando se busca un ID que no existe: se responde 404
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
