package ar.edu.utn.dsi.bolsines.boundary.interfaces;

import ar.edu.utn.dsi.bolsines.dto.MapaBolsines;
import ar.edu.utn.dsi.bolsines.dto.PosicionBolsin;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <<boundary>> InterfazGoogleMaps
 * Arma el mapa a visualizar a partir de las coordenadas de los bolsines: calcula el centro y el zoom.
 * El dibujo lo hace la librería de mapas en la pantalla web.
 * Nota de implementación: se usa Leaflet + OpenStreetMap en lugar de la librería de Google Maps,
 * porque Google Maps exige una API key paga; la responsabilidad de esta clase es la misma.
 */
@Component
public class InterfazGoogleMaps {

    private static final double LATITUD_ARGENTINA = -34.0;
    private static final double LONGITUD_ARGENTINA = -64.0;

    public MapaBolsines obtenerMapa(List<PosicionBolsin> posiciones) {
        if (posiciones.isEmpty()) {
            return new MapaBolsines(LATITUD_ARGENTINA, LONGITUD_ARGENTINA, 4, posiciones);
        }
        double latitudMinima = Double.MAX_VALUE;
        double latitudMaxima = -Double.MAX_VALUE;
        double longitudMinima = Double.MAX_VALUE;
        double longitudMaxima = -Double.MAX_VALUE;
        for (PosicionBolsin posicion : posiciones) {
            double latitud = posicion.getLatitud();
            double longitud = posicion.getLongitud();
            latitudMinima = Math.min(latitudMinima, latitud);
            latitudMaxima = Math.max(latitudMaxima, latitud);
            longitudMinima = Math.min(longitudMinima, longitud);
            longitudMaxima = Math.max(longitudMaxima, longitud);
        }
        double latitudCentro = (latitudMinima + latitudMaxima) / 2;
        double longitudCentro = (longitudMinima + longitudMaxima) / 2;
        int zoom = calcularZoom(Math.max(latitudMaxima - latitudMinima, longitudMaxima - longitudMinima));
        return new MapaBolsines(latitudCentro, longitudCentro, zoom, posiciones);
    }

    /** Cuanto más separados están los bolsines (en grados), más lejos se ve el mapa. */
    private int calcularZoom(double extensionEnGrados) {
        if (extensionEnGrados > 16) return 4;
        if (extensionEnGrados > 8) return 5;
        if (extensionEnGrados > 4) return 6;
        if (extensionEnGrados > 2) return 7;
        return 9;
    }
}
