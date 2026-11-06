package pe.edu.tecsup.cliente.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.cliente.dto.ClienteRequest;
import pe.edu.tecsup.cliente.dto.ClienteResponse;
import pe.edu.tecsup.cliente.exception.RecursoNoEncontradoException;
import pe.edu.tecsup.cliente.exception.ReglaNegocioException;
import pe.edu.tecsup.cliente.model.Cliente;
import pe.edu.tecsup.cliente.repository.ClienteRepository;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Inyección de dependencias por constructor: Spring entrega el repositorio
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return ClienteResponse.desde(obtener(id));
    }

    @Transactional
    public ClienteResponse registrar(ClienteRequest request) {
        if (clienteRepository.existsByDni(request.dni())) {
            throw new ReglaNegocioException("Ya existe un cliente con el DNI " + request.dni());
        }
        if (clienteRepository.existsByCorreo(request.correo())) {
            throw new ReglaNegocioException("Ya existe un cliente con el correo " + request.correo());
        }
        if (clienteRepository.existsByNumeroLicencia(request.numeroLicencia())) {
            throw new ReglaNegocioException("Ya existe un cliente con la licencia " + request.numeroLicencia());
        }

        Cliente cliente = new Cliente();
        copiarDatos(request, cliente);
        return ClienteResponse.desde(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = obtener(id);

        // Los datos únicos no pueden repetirse con OTRO cliente (con él mismo sí)
        if (clienteRepository.existsByDniAndIdNot(request.dni(), id)) {
            throw new ReglaNegocioException("Ya existe otro cliente con el DNI " + request.dni());
        }
        if (clienteRepository.existsByCorreoAndIdNot(request.correo(), id)) {
            throw new ReglaNegocioException("Ya existe otro cliente con el correo " + request.correo());
        }
        if (clienteRepository.existsByNumeroLicenciaAndIdNot(request.numeroLicencia(), id)) {
            throw new ReglaNegocioException("Ya existe otro cliente con la licencia " + request.numeroLicencia());
        }

        copiarDatos(request, cliente);
        return ClienteResponse.desde(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminar(Long id) {
        clienteRepository.delete(obtener(id));
    }

    private Cliente obtener(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + id));
    }

    private void copiarDatos(ClienteRequest request, Cliente cliente) {
        cliente.setDni(request.dni());
        cliente.setNombres(request.nombres().trim());
        cliente.setApellidos(request.apellidos().trim());
        cliente.setCorreo(request.correo().trim().toLowerCase());
        cliente.setTelefono(request.telefono());
        cliente.setNumeroLicencia(request.numeroLicencia());
    }
}
