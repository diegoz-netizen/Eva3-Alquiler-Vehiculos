package pe.edu.tecsup.vehiculo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.vehiculo.model.EstadoVehiculo;
import pe.edu.tecsup.vehiculo.model.Vehiculo;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, Long id);

    List<Vehiculo> findByEstado(EstadoVehiculo estado);
}
