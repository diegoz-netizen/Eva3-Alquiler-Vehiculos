package pe.edu.tecsup.alquiler.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.tecsup.alquiler.dto.AlquilerRequest;
import pe.edu.tecsup.alquiler.dto.AlquilerResponse;
import pe.edu.tecsup.alquiler.dto.EstadoRequest;
import pe.edu.tecsup.alquiler.services.AlquilerService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    // GET /api/alquileres o GET /api/alquileres?idCliente=1
    @GetMapping
    public List<AlquilerResponse> listar(@RequestParam(required = false) Long idCliente) {
        return alquilerService.listar(idCliente);
    }

    @GetMapping("/{id}")
    public AlquilerResponse buscarPorId(@PathVariable Long id) {
        return alquilerService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlquilerResponse> registrar(@Valid @RequestBody AlquilerRequest request) {
        AlquilerResponse creado = alquilerService.registrar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public AlquilerResponse actualizar(@PathVariable Long id, @Valid @RequestBody AlquilerRequest request) {
        return alquilerService.actualizar(id, request);
    }

    // Finalizar (devolución) o cancelar un alquiler
    @PatchMapping("/{id}/estado")
    public AlquilerResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest request) {
        return alquilerService.cambiarEstado(id, request.estado());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alquilerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
