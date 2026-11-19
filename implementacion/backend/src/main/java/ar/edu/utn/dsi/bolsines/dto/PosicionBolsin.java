package ar.edu.utn.dsi.bolsines.dto;

import java.time.LocalDateTime;

/** Datos de un bolsín enviado y su ubicación, tal como se muestran en el mapa. */
public class PosicionBolsin {

    private final int numeroBolsin;
    private final int numeroPrecinto;
    private final String codigoCMDestino;
    private final String nombreCMDestino;
    private final String modeloGPSTracker;
    private final double latitud;
    private final double longitud;
    private final LocalDateTime fechaHoraUltimaActualizacion;

    public PosicionBolsin(int numeroBolsin, int numeroPrecinto, String codigoCMDestino, String nombreCMDestino,
                          String modeloGPSTracker, double latitud, double longitud,
                          LocalDateTime fechaHoraUltimaActualizacion) {
        this.numeroBolsin = numeroBolsin;
        this.numeroPrecinto = numeroPrecinto;
        this.codigoCMDestino = codigoCMDestino;
        this.nombreCMDestino = nombreCMDestino;
        this.modeloGPSTracker = modeloGPSTracker;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaHoraUltimaActualizacion = fechaHoraUltimaActualizacion;
    }

    public int getNumeroBolsin() {
        return numeroBolsin;
    }

    public int getNumeroPrecinto() {
        return numeroPrecinto;
    }

    public String getCodigoCMDestino() {
        return codigoCMDestino;
    }

    public String getNombreCMDestino() {
        return nombreCMDestino;
    }

    public String getModeloGPSTracker() {
        return modeloGPSTracker;
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
