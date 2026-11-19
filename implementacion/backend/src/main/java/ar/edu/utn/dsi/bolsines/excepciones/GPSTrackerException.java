package ar.edu.utn.dsi.bolsines.excepciones;

// Error cuando el GPS Tracker no puede informar la ubicación (flujo alternativo A3)
public class GPSTrackerException extends RuntimeException {

    public GPSTrackerException(String mensaje) {
        super(mensaje);
    }
}
