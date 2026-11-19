package ar.edu.utn.dsi.bolsines.patrones.adapter;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos.ServicioGPSTrackerGeoPulseMTR900;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * PATRÓN ADAPTER - Adaptador concreto para el GPS Tracker modelo GeoPulse MTR-900.
 * Envuelve al ServicioGPSTrackerGeoPulseMTR900 y traduce su matriz al formato común
 * UbicacionBolsin. Este modelo solo pide el número de bolsín (no usa códigos de CM).
 */
public class AdaptadorGPSTrackerGeoPulseMTR900 implements IAdaptadorGPSTracker {

    private final String claveAPI;
    private final ServicioGPSTrackerGeoPulseMTR900 servicio;
    private int numeroBolsinSolicitado;
    private Object[][] ubicacionRecibida;
    private UbicacionBolsin ubicacionAdaptada;

    public AdaptadorGPSTrackerGeoPulseMTR900(String claveAPI) {
        this.claveAPI = claveAPI;
        this.servicio = new ServicioGPSTrackerGeoPulseMTR900();
    }

    @Override
    public UbicacionBolsin obtenerUbicacionBolsin(int numeroBolsin, String codigoCMOrigen, String codigoCMDestino) {
        solicitarUbicacionBolsin(numeroBolsin, codigoCMOrigen, codigoCMDestino);
        adaptarUbicacionBolsin();
        return ubicacionAdaptada;
    }

    @Override
    public void solicitarUbicacionBolsin(int numeroBolsin, String codigoCMOrigen, String codigoCMDestino) {
        numeroBolsinSolicitado = numeroBolsin;
        ubicacionRecibida = servicio.fetchCargoPositions(claveAPI, numeroBolsin);
    }

    /**
     * Matriz recibida: [ [numeroBolsin, latitud, longitud, "fechaHora"] ].
     * Si la fila no tiene los 4 valores, alguno no es válido o es de otro bolsín, se rechaza (A3).
     */
    @Override
    public void adaptarUbicacionBolsin() {
        int numero;
        double latitud;
        double longitud;
        LocalDateTime fechaHora;
        try {
            Object[] fila = ubicacionRecibida[0];
            if (fila.length != 4) {
                throw new IllegalArgumentException("se esperaban 4 valores");
            }
            numero = ((Integer) fila[0]);
            latitud = ((Number) fila[1]).doubleValue();
            longitud = ((Number) fila[2]).doubleValue();
            fechaHora = LocalDateTime.parse((String) fila[3]);
        } catch (Exception e) {
            throw new GPSTrackerException("Respuesta inválida del GPS Tracker GeoPulse MTR-900: "
                    + Arrays.deepToString(ubicacionRecibida));
        }
        if (numero != numeroBolsinSolicitado) {
            throw new GPSTrackerException("El GPS Tracker GeoPulse MTR-900 informó otro bolsín: "
                    + Arrays.deepToString(ubicacionRecibida));
        }
        ubicacionAdaptada = new UbicacionBolsin(numero, latitud, longitud, fechaHora);
    }
}
