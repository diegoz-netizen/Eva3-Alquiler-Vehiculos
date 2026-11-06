package pe.edu.tecsup.alquiler.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Tabla "alquileres" de la base de datos bd_alquiler
@Entity
@Table(name = "alquileres")
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencias a los otros microservicios: solo se guarda el ID, no hay clave foránea
    // porque el cliente y el vehículo viven en otras bases de datos
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "id_vehiculo", nullable = false)
    private Long idVehiculo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    // BigDecimal para dinero: double tiene errores de redondeo
    @Column(name = "tarifa_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaDiaria;

    @Column(nullable = false)
    private Integer dias;

    @Column(name = "importe_estimado", nullable = false, precision = 12, scale = 2)
    private BigDecimal importeEstimado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoAlquiler estado;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    void alRegistrar() {
        fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public BigDecimal getImporteEstimado() {
        return importeEstimado;
    }

    public void setImporteEstimado(BigDecimal importeEstimado) {
        this.importeEstimado = importeEstimado;
    }

    public EstadoAlquiler getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlquiler estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
