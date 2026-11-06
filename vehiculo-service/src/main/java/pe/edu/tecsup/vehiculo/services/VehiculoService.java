package pe.edu.tecsup.vehiculo.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.vehiculo.dto.VehiculoRequest;
import pe.edu.tecsup.vehiculo.dto.VehiculoResponse;
import pe.edu.tecsup.vehiculo.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.vehiculo.exception.ReglaNegocioException;
import pe.edu.tecsup.vehiculo.model.EstadoVehiculo;
import pe.edu.tecsup.vehiculo.model.Vehiculo;
import pe.edu.tecsup.vehiculo.repository.VehiculoRepository;

import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // Si llega un estado, filtra por él; si no, lista todos
    @Transactional(readOnly = true)
    public List<VehiculoResponse> listar(EstadoVehiculo estado) {
        List<Vehiculo> vehiculos = (estado == null)
                ? vehiculoRepository.findAll()
                : vehiculoRepository.findByEstado(estado);
        return vehiculos.stream()
                .map(VehiculoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehiculoResponse buscarPorId(Long id) {
        return VehiculoResponse.desde(obtener(id));
    }

    @Transactional
    public VehiculoResponse registrar(VehiculoRequest request) {
        if (vehiculoRepository.existsByPlaca(request.placa())) {
            throw new ReglaNegocioException("Ya existe un vehículo con la placa " + request.placa());
        }

        Vehiculo vehiculo = new Vehiculo();
        copiarDatos(request, vehiculo);
        return VehiculoResponse.desde(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public VehiculoResponse actualizar(Long id, VehiculoRequest request) {
        Vehiculo vehiculo = obtener(id);

        if (vehiculoRepository.existsByPlacaAndIdNot(request.placa(), id)) {
            throw new ReglaNegocioException("Ya existe otro vehículo con la placa " + request.placa());
        }

        copiarDatos(request, vehiculo);
        return VehiculoResponse.desde(vehiculoRepository.save(vehiculo));
    }

    @Transactional
    public void eliminar(Long id) {
        vehiculoRepository.delete(obtener(id));
    }

    private Vehiculo obtener(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehículo con id " + id));
    }

    private void copiarDatos(VehiculoRequest request, Vehiculo vehiculo) {
        vehiculo.setPlaca(request.placa());
        vehiculo.setMarca(request.marca().trim());
        vehiculo.setModelo(request.modelo().trim());
        vehiculo.setAnio(request.anio());
        vehiculo.setColor(request.color());
        vehiculo.setEstado(request.estado() != null ? request.estado() : EstadoVehiculo.DISPONIBLE);
    }
}
