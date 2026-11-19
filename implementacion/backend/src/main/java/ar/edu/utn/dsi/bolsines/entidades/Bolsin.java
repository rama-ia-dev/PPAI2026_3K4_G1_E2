package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <<entity>> Bolsin
 * modeloGPSTracker: decisión de diseño. El requerimiento no funcional 6 de la ERS indica que cada bolsín cuenta con
 * un dispositivo GPS Tracker; el modelo de ese dispositivo define qué adaptador se usa.
 */
@Entity
public class Bolsin {

    @Id
    private int numeroBolsin;
    private LocalDate fecha;
    private int numeroPrecinto;
    private double peso; // en gramos
    private String modeloGPSTracker;

    @ManyToOne(fetch = FetchType.EAGER)
    private ComisionMedica origen;

    @ManyToOne(fetch = FetchType.EAGER)
    private ComisionMedica destino;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bolsin_numero")
    private List<CambioEstadoBolsin> cambioEstado = new ArrayList<>();

    protected Bolsin() {
    }

    public Bolsin(int numeroBolsin, LocalDate fecha, int numeroPrecinto, double peso,
                  String modeloGPSTracker, ComisionMedica origen, ComisionMedica destino) {
        this.numeroBolsin = numeroBolsin;
        this.fecha = fecha;
        this.numeroPrecinto = numeroPrecinto;
        this.peso = peso;
        this.modeloGPSTracker = modeloGPSTracker;
        this.origen = origen;
        this.destino = destino;
    }

    // ---------- Métodos usados en el CU 36 ----------

    public boolean esTuCMOrigen(ComisionMedica cm) {
        return origen.equals(cm);
    }

    /** Recorre sus cambios de estado, toma el actual y le pregunta si es Enviado. */
    public boolean sosEnviado() {
        for (CambioEstadoBolsin cambio : cambioEstado) {
            if (cambio.sosActual()) {
                return cambio.sosEnviado();
            }
        }
        return false;
    }

    public int getNumeroBolsin() {
        return numeroBolsin;
    }

    public int getNroPrecinto() {
        return numeroPrecinto;
    }

    public String getModeloGPSTracker() {
        return modeloGPSTracker;
    }

    public ComisionMedica obtenerCMDestino() {
        return destino;
    }

    // ---------- Soporte para la carga de datos de prueba (CU 19 Generar bolsín, CU 39 Cerrar bolsín y CU 27 Registrar el retiro de bolsines) ----------

    public void cambiarEstado(Estado nuevoEstado, Empleado responsable, LocalDateTime fechaHora) {
        for (CambioEstadoBolsin cambio : cambioEstado) {
            if (cambio.sosActual()) {
                cambio.setFechaHoraFin(fechaHora);
            }
        }
        cambioEstado.add(new CambioEstadoBolsin(fechaHora, nuevoEstado, responsable));
    }
}
