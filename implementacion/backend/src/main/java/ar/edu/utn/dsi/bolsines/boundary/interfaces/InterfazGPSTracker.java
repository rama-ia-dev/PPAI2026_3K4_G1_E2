package ar.edu.utn.dsi.bolsines.boundary.interfaces;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import ar.edu.utn.dsi.bolsines.patrones.adapter.AdaptadorGPSTrackerGeoPulseMTR900;
import ar.edu.utn.dsi.bolsines.patrones.adapter.AdaptadorGPSTrackerNavTrackQX7A;
import ar.edu.utn.dsi.bolsines.patrones.adapter.AdaptadorGPSTrackerXTR4500L;
import ar.edu.utn.dsi.bolsines.patrones.adapter.IAdaptadorGPSTracker;
import ar.edu.utn.dsi.bolsines.patrones.adapter.UbicacionBolsin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <<boundary>> InterfazGPSTracker
 * PATRÓN ADAPTER - Cliente: crea el adaptador que corresponde al modelo de GPS Tracker
 * del bolsín y siempre le habla a través de IAdaptadorGPSTracker, sin conocer el formato
 * de cada modelo. Guarda la clave API de cada modelo.
 */
@Component
public class InterfazGPSTracker {

    public static final String MODELO_XTR_4500L = "XTR-4500L";
    public static final String MODELO_NAVTRACK_QX_7A = "NavTrack QX-7A";
    public static final String MODELO_GEOPULSE_MTR_900 = "GeoPulse MTR-900";

    private final String claveAPIXTR4500L;
    private final String claveAPINavTrackQX7A;
    private final String claveAPIGeoPulseMTR900;

    public InterfazGPSTracker(@Value("${gps.tracker.clave-api.xtr4500l}") String claveAPIXTR4500L,
                              @Value("${gps.tracker.clave-api.navtrackqx7a}") String claveAPINavTrackQX7A,
                              @Value("${gps.tracker.clave-api.geopulsemtr900}") String claveAPIGeoPulseMTR900) {
        this.claveAPIXTR4500L = claveAPIXTR4500L;
        this.claveAPINavTrackQX7A = claveAPINavTrackQX7A;
        this.claveAPIGeoPulseMTR900 = claveAPIGeoPulseMTR900;
    }

    public UbicacionBolsin obtenerUbicacionBolsin(int numeroBolsin, String codigoCMOrigen,
                                                  String codigoCMDestino, String modeloGPSTracker) {
        IAdaptadorGPSTracker adaptador = crearAdaptador(modeloGPSTracker);
        return adaptador.obtenerUbicacionBolsin(numeroBolsin, codigoCMOrigen, codigoCMDestino);
    }

    private IAdaptadorGPSTracker crearAdaptador(String modeloGPSTracker) {
        if (MODELO_XTR_4500L.equals(modeloGPSTracker)) {
            return new AdaptadorGPSTrackerXTR4500L(claveAPIXTR4500L);
        }
        if (MODELO_NAVTRACK_QX_7A.equals(modeloGPSTracker)) {
            return new AdaptadorGPSTrackerNavTrackQX7A(claveAPINavTrackQX7A);
        }
        if (MODELO_GEOPULSE_MTR_900.equals(modeloGPSTracker)) {
            return new AdaptadorGPSTrackerGeoPulseMTR900(claveAPIGeoPulseMTR900);
        }
        throw new GPSTrackerException("Modelo de GPS Tracker no soportado: " + modeloGPSTracker);
    }
}
