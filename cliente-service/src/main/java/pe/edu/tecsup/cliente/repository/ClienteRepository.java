package pe.edu.tecsup.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Consultas derivadas: Spring arma el SQL a partir del nombre del método
    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoAndIdNot(String correo, Long id);

    boolean existsByNumeroLicencia(String numeroLicencia);

    boolean existsByNumeroLicenciaAndIdNot(String numeroLicencia, Long id);
}
