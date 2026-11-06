package pe.edu.tecsup.alquiler.exception;

// Se lanza cuando cliente-service o vehiculo-service no responden: 503
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
