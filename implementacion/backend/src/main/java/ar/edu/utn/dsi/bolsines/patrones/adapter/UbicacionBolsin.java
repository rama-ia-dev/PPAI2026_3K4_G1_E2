package ar.edu.utn.dsi.bolsines.patrones.adapter;

import java.time.LocalDateTime;

/**
 * Formato común de ubicación que devuelven todos los adaptadores,
 * sin importar el modelo de GPS Tracker que la informó.
 */
public class UbicacionBolsin {

    private final int numeroBolsin;
    private final double latitud;
    private final double longitud;
    private final LocalDateTime fechaHoraUltimaActualizacion;

    public UbicacionBolsin(int numeroBolsin, double latitud, double longitud,
                           LocalDateTime fechaHoraUltimaActualizacion) {
        this.numeroBolsin = numeroBolsin;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public LocalDateTime getFechaHoraUltimaActualizacion() {
        return fechaHoraUltimaActualizacion;
    }
}
