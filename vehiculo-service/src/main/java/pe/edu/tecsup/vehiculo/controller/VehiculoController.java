package pe.edu.tecsup.vehiculo.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.tecsup.vehiculo.dto.VehiculoRequest;
import pe.edu.tecsup.vehiculo.dto.VehiculoResponse;
import pe.edu.tecsup.vehiculo.model.EstadoVehiculo;
import pe.edu.tecsup.vehiculo.services.VehiculoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    // GET /api/vehiculos o GET /api/vehiculos?estado=DISPONIBLE
    @GetMapping
    public List<VehiculoResponse> listar(@RequestParam(required = false) EstadoVehiculo estado) {
        return vehiculoService.listar(estado);
    }

    @GetMapping("/{id}")
    public VehiculoResponse buscarPorId(@PathVariable Long id) {
        return vehiculoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<VehiculoResponse> registrar(@Valid @RequestBody VehiculoRequest request) {
        VehiculoResponse creado = vehiculoService.registrar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public VehiculoResponse actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoRequest request) {
        return vehiculoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
