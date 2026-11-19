package ar.edu.utn.dsi.bolsines.patrones.adapter;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos.ServicioGPSTrackerXTR4500L;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

/**
 * PATRÓN ADAPTER - Adaptador concreto para el GPS Tracker modelo XTR-4500L.
 * Envuelve al ServicioGPSTrackerXTR4500L y traduce su JSON al formato común UbicacionBolsin.
 * Este modelo pide el código de la CM origen (el de CM destino no lo usa).
 */
public class AdaptadorGPSTrackerXTR4500L implements IAdaptadorGPSTracker {

    private final String claveAPI;
    private final ServicioGPSTrackerXTR4500L servicio;
    private int numeroBolsinSolicitado;
    private String ubicacionRecibida;
    private UbicacionBolsin ubicacionAdaptada;

    public AdaptadorGPSTrackerXTR4500L(String claveAPI) {
        this.claveAPI = claveAPI;
        this.servicio = new ServicioGPSTrackerXTR4500L();
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
        ubicacionRecibida = servicio.getBolsinLocation(claveAPI, numeroBolsin, codigoCMOrigen);
    }

    /**
     * JSON recibido: { "items": [ { numeroBolsin, latitud, longitud, fechaHoraUltimaActualizacion } ] }.
     * Si falta un dato, no es del tipo esperado o es de otro bolsín, la respuesta se rechaza (A3).
     */
    @Override
    public void adaptarUbicacionBolsin() {
        JsonNode item;
        try {
            item = new ObjectMapper().readTree(ubicacionRecibida).path("items").path(0);
        } catch (Exception e) {
            throw new GPSTrackerException("Respuesta inválida del GPS Tracker XTR-4500L: " + ubicacionRecibida);
        }
        JsonNode numero = item.path("numeroBolsin");
        JsonNode latitud = item.path("latitud");
        JsonNode longitud = item.path("longitud");
        JsonNode fechaHora = item.path("fechaHoraUltimaActualizacion");
        if (!numero.isInt() || !latitud.isNumber() || !longitud.isNumber() || !fechaHora.isTextual()) {
            throw new GPSTrackerException("Respuesta incompleta del GPS Tracker XTR-4500L: " + ubicacionRecibida);
        }
        if (numero.asInt() != numeroBolsinSolicitado) {
            throw new GPSTrackerException("El GPS Tracker XTR-4500L informó otro bolsín: " + ubicacionRecibida);
        }
        try {
            ubicacionAdaptada = new UbicacionBolsin(numero.asInt(), latitud.asDouble(), longitud.asDouble(),
                    LocalDateTime.parse(fechaHora.asText()));
        } catch (Exception e) {
            throw new GPSTrackerException("Fecha inválida del GPS Tracker XTR-4500L: " + ubicacionRecibida);
        }
    }
}
