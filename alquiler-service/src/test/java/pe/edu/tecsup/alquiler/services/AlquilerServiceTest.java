package pe.edu.tecsup.alquiler.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import pe.edu.tecsup.alquiler.dto.AlquilerRequest;
import pe.edu.tecsup.alquiler.dto.AlquilerResponse;
import pe.edu.tecsup.alquiler.dto.ClienteDTO;
import pe.edu.tecsup.alquiler.dto.VehiculoDTO;
import pe.edu.tecsup.alquiler.exception.ReglaNegocioException;
import pe.edu.tecsup.alquiler.model.Alquiler;
import pe.edu.tecsup.alquiler.model.EstadoAlquiler;
import pe.edu.tecsup.alquiler.repository.AlquilerRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

// Pruebas unitarias: sin base de datos ni otros servicios (se simulan con Mockito)
class AlquilerServiceTest {

    private final AlquilerRepository repository = mock(AlquilerRepository.class);
    private final ClienteClient clienteClient = mock(ClienteClient.class);
    private final VehiculoClient vehiculoClient = mock(VehiculoClient.class);
    private final AlquilerService service = new AlquilerService(repository, clienteClient, vehiculoClient);

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private final LocalDate hoy = LocalDate.now();

    @Test
    void calculaDiasEImporteEstimado() {
        int dias = AlquilerService.calcularDias(hoy, hoy.plusDays(3));

        assertThat(dias).isEqualTo(3);
        assertThat(AlquilerService.calcularImporte(dias, new BigDecimal("120.50")))
                .isEqualByComparingTo("361.50");
    }

    @Test
    void registraAlquilerActivoConImporteCalculado() {
        when(clienteClient.obtener(1L)).thenReturn(new ClienteDTO(1L, "Ana", "Rojas"));
        when(vehiculoClient.obtener(2L)).thenReturn(new VehiculoDTO(2L, "ABC-123", "DISPONIBLE"));
        when(repository.existeCruceDeFechas(eq(2L), eq(EstadoAlquiler.ACTIVO), any(), any(), isNull())).thenReturn(false);
        when(repository.save(any(Alquiler.class))).thenAnswer(inv -> inv.getArgument(0));

        AlquilerResponse respuesta = service.registrar(
                new AlquilerRequest(1L, 2L, hoy, hoy.plusDays(4), new BigDecimal("100.00")));

        assertThat(respuesta.dias()).isEqualTo(4);
        assertThat(respuesta.importeEstimado()).isEqualByComparingTo("400.00");
        assertThat(respuesta.estado()).isEqualTo(EstadoAlquiler.ACTIVO);
    }

    @Test
    void rechazaVehiculoQueNoEstaDisponible() {
        when(clienteClient.obtener(1L)).thenReturn(new ClienteDTO(1L, "Ana", "Rojas"));
        when(vehiculoClient.obtener(2L)).thenReturn(new VehiculoDTO(2L, "ABC-123", "MANTENIMIENTO"));

        assertThatThrownBy(() -> service.registrar(
                new AlquilerRequest(1L, 2L, hoy, hoy.plusDays(2), new BigDecimal("80.00"))))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("no está disponible");
        verify(repository, never()).save(any());
    }

    @Test
    void rechazaVehiculoConFechasCruzadas() {
        when(clienteClient.obtener(1L)).thenReturn(new ClienteDTO(1L, "Ana", "Rojas"));
        when(vehiculoClient.obtener(2L)).thenReturn(new VehiculoDTO(2L, "ABC-123", "DISPONIBLE"));
        when(repository.existeCruceDeFechas(eq(2L), eq(EstadoAlquiler.ACTIVO), any(), any(), isNull())).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(
                new AlquilerRequest(1L, 2L, hoy, hoy.plusDays(2), new BigDecimal("80.00"))))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("ya tiene un alquiler activo");
    }

    @Test
    void elDtoRechazaFechaFinAnteriorYTarifaNegativa() {
        AlquilerRequest invalido = new AlquilerRequest(1L, 2L, hoy.plusDays(5), hoy.plusDays(2), new BigDecimal("-10"));

        Set<ConstraintViolation<AlquilerRequest>> errores = validator.validate(invalido);

        assertThat(errores).extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("rangoDeFechasValido", "tarifaDiaria");
    }

    @Test
    void elDtoRechazaFechaFinIgualAlInicio() {
        AlquilerRequest mismoDia = new AlquilerRequest(1L, 2L, hoy, hoy, new BigDecimal("50"));

        assertThat(validator.validate(mismoDia)).extracting(ConstraintViolation::getMessage)
                .containsExactly("La fecha de fin debe ser posterior a la fecha de inicio");
    }
}
