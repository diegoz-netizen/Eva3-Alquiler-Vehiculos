package pe.edu.tecsup.alquiler.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.alquiler.dto.AlquilerRequest;
import pe.edu.tecsup.alquiler.dto.AlquilerResponse;
import pe.edu.tecsup.alquiler.dto.VehiculoDTO;
import pe.edu.tecsup.alquiler.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.alquiler.exception.ReglaNegocioException;
import pe.edu.tecsup.alquiler.model.Alquiler;
import pe.edu.tecsup.alquiler.model.EstadoAlquiler;
import pe.edu.tecsup.alquiler.repository.AlquilerRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;

    public AlquilerService(AlquilerRepository alquilerRepository,
                           ClienteClient clienteClient,
                           VehiculoClient vehiculoClient) {
        this.alquilerRepository = alquilerRepository;
        this.clienteClient = clienteClient;
        this.vehiculoClient = vehiculoClient;
    }

    @Transactional(readOnly = true)
    public List<AlquilerResponse> listar(Long idCliente) {
        List<Alquiler> alquileres = (idCliente == null)
                ? alquilerRepository.findAll()
                : alquilerRepository.findByIdCliente(idCliente);
        return alquileres.stream()
                .map(AlquilerResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlquilerResponse buscarPorId(Long id) {
        return AlquilerResponse.desde(obtener(id));
    }

    @Transactional
    public AlquilerResponse registrar(AlquilerRequest request) {
        validarReferenciasYDisponibilidad(request, null);

        Alquiler alquiler = new Alquiler();
        copiarDatos(request, alquiler);
        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        return AlquilerResponse.desde(alquilerRepository.save(alquiler));
    }

    @Transactional
    public AlquilerResponse actualizar(Long id, AlquilerRequest request) {
        Alquiler alquiler = obtener(id);

        if (alquiler.getEstado() != EstadoAlquiler.ACTIVO) {
            throw new ReglaNegocioException("Solo se puede modificar un alquiler ACTIVO; este está " + alquiler.getEstado());
        }
        validarReferenciasYDisponibilidad(request, id);

        copiarDatos(request, alquiler);
        return AlquilerResponse.desde(alquilerRepository.save(alquiler));
    }

    // Un alquiler ACTIVO pasa a FINALIZADO (se devolvió) o CANCELADO; después ya no cambia
    @Transactional
    public AlquilerResponse cambiarEstado(Long id, EstadoAlquiler nuevoEstado) {
        Alquiler alquiler = obtener(id);

        if (alquiler.getEstado() != EstadoAlquiler.ACTIVO) {
            throw new ReglaNegocioException("El alquiler ya está " + alquiler.getEstado() + " y no puede cambiar de estado");
        }
        if (nuevoEstado == EstadoAlquiler.ACTIVO) {
            throw new ReglaNegocioException("El alquiler ya está ACTIVO");
        }

        alquiler.setEstado(nuevoEstado);
        return AlquilerResponse.desde(alquilerRepository.save(alquiler));
    }

    @Transactional
    public void eliminar(Long id) {
        alquilerRepository.delete(obtener(id));
    }

    // Días entre las dos fechas. El DTO ya garantiza que fechaFin es posterior, así que siempre hay al menos 1.
    static int calcularDias(LocalDate fechaInicio, LocalDate fechaFin) {
        return (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    // Importe estimado = días x tarifa diaria, redondeado a 2 decimales
    static BigDecimal calcularImporte(int dias, BigDecimal tarifaDiaria) {
        return tarifaDiaria.multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);
    }

    private void validarReferenciasYDisponibilidad(AlquilerRequest request, Long idExcluir) {
        // Si el cliente o el vehículo no existen, estas llamadas lanzan 404
        clienteClient.obtener(request.idCliente());
        VehiculoDTO vehiculo = vehiculoClient.obtener(request.idVehiculo());

        if (!"DISPONIBLE".equals(vehiculo.estado())) {
            throw new ReglaNegocioException("El vehículo " + vehiculo.placa() + " no está disponible (estado: " + vehiculo.estado() + ")");
        }

        boolean cruce = alquilerRepository.existeCruceDeFechas(
                request.idVehiculo(), EstadoAlquiler.ACTIVO,
                request.fechaInicio(), request.fechaFin(), idExcluir);
        if (cruce) {
            throw new ReglaNegocioException("El vehículo " + vehiculo.placa() + " ya tiene un alquiler activo en esas fechas");
        }
    }

    private Alquiler obtener(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un alquiler con id " + id));
    }

    private void copiarDatos(AlquilerRequest request, Alquiler alquiler) {
        int dias = calcularDias(request.fechaInicio(), request.fechaFin());

        alquiler.setIdCliente(request.idCliente());
        alquiler.setIdVehiculo(request.idVehiculo());
        alquiler.setFechaInicio(request.fechaInicio());
        alquiler.setFechaFin(request.fechaFin());
        alquiler.setTarifaDiaria(request.tarifaDiaria());
        alquiler.setDias(dias);
        alquiler.setImporteEstimado(calcularImporte(dias, request.tarifaDiaria()));
    }
}
