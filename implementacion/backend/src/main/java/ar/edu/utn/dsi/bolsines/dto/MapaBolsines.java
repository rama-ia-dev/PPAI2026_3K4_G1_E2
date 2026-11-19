package ar.edu.utn.dsi.bolsines.dto;

import java.util.List;

/** Mapa a visualizar: centro, nivel de zoom y posiciones de los bolsines. */
public class MapaBolsines {

    private final double latitudCentro;
    private final double longitudCentro;
    private final int zoom;
    private final List<PosicionBolsin> posiciones;

    public MapaBolsines(double latitudCentro, double longitudCentro, int zoom, List<PosicionBolsin> posiciones) {
        this.latitudCentro = latitudCentro;
        this.longitudCentro = longitudCentro;
        this.zoom = zoom;
        this.posiciones = posiciones;
    }

    public double getLatitudCentro() {
        return latitudCentro;
    }

    public double getLongitudCentro() {
        return longitudCentro;
    }

    public int getZoom() {
        return zoom;
    }

    public List<PosicionBolsin> getPosiciones() {
        return posiciones;
    }
}
