package ar.edu.utn.dsi.bolsines.patrones.adapter;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos.ServicioGPSTrackerNavTrackQX7A;

import java.time.LocalDateTime;

/**
 * PATRÓN ADAPTER - Adaptador concreto para el GPS Tracker modelo NavTrack QX-7A.
 * Envuelve al ServicioGPSTrackerNavTrackQX7A y traduce su String separado por comas
 * al formato común UbicacionBolsin. Este modelo pide el código de la CM destino.
 */
public class AdaptadorGPSTrackerNavTrackQX7A implements IAdaptadorGPSTracker {

    private final String claveAPI;
    private final ServicioGPSTrackerNavTrackQX7A servicio;
    private int numeroBolsinSolicitado;
    private String ubicacionRecibida;
    private UbicacionBolsin ubicacionAdaptada;

    public AdaptadorGPSTrackerNavTrackQX7A(String claveAPI) {
        this.claveAPI = claveAPI;
        this.servicio = new ServicioGPSTrackerNavTrackQX7A();
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
        ubicacionRecibida = servicio.retrieveTrackingData(claveAPI, numeroBolsin, codigoCMDestino);
    }

    /**
     * String recibido: "numeroBolsin,latitud,longitud,fechaHora".
     * Si no tiene los 4 ítems, alguno no es válido o es de otro bolsín, la respuesta se rechaza (A3).
     */
    @Override
    public void adaptarUbicacionBolsin() {
        int numero;
        double latitud;
        double longitud;
        LocalDateTime fechaHora;
        try {
            String[] items = ubicacionRecibida.trim().split(",");
            if (items.length != 4) {
                throw new IllegalArgumentException("se esperaban 4 ítems");
            }
            numero = Integer.parseInt(items[0].trim());
            latitud = Double.parseDouble(items[1].trim());
            longitud = Double.parseDouble(items[2].trim());
            fechaHora = LocalDateTime.parse(items[3].trim());
        } catch (Exception e) {
            throw new GPSTrackerException("Respuesta inválida del GPS Tracker NavTrack QX-7A: " + ubicacionRecibida);
        }
        if (numero != numeroBolsinSolicitado) {
            throw new GPSTrackerException("El GPS Tracker NavTrack QX-7A informó otro bolsín: " + ubicacionRecibida);
        }
        ubicacionAdaptada = new UbicacionBolsin(numero, latitud, longitud, fechaHora);
    }
}
