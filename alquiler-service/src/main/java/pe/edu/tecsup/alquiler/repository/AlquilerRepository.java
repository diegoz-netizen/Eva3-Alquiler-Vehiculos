package pe.edu.tecsup.alquiler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.tecsup.alquiler.model.Alquiler;
import pe.edu.tecsup.alquiler.model.EstadoAlquiler;

import java.time.LocalDate;
import java.util.List;

public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {

    List<Alquiler> findByIdCliente(Long idCliente);

    // ¿El vehículo ya tiene un alquiler activo que se cruza con esas fechas?
    // Dos rangos se cruzan si uno empieza antes de que el otro termine, y viceversa.
    // idExcluir sirve al editar: el alquiler no debe chocar consigo mismo.
    @Query("""
            select count(a) > 0 from Alquiler a
            where a.idVehiculo = :idVehiculo
              and a.estado = :estado
              and a.fechaInicio < :fechaFin
              and a.fechaFin > :fechaInicio
              and (:idExcluir is null or a.id <> :idExcluir)
            """)
    boolean existeCruceDeFechas(@Param("idVehiculo") Long idVehiculo,
                                @Param("estado") EstadoAlquiler estado,
                                @Param("fechaInicio") LocalDate fechaInicio,
                                @Param("fechaFin") LocalDate fechaFin,
                                @Param("idExcluir") Long idExcluir);
}
