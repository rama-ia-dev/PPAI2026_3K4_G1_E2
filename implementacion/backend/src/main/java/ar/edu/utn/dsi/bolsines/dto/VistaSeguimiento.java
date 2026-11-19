package ar.edu.utn.dsi.bolsines.dto;

import java.util.List;

/**
 * Estado de la PantallaConsultarSeguimientoBolsines que se envía al navegador para que lo dibuje.
 * paso: INICIO, SELECCION (muestra el mapa), CONFIRMACION (pide confirmar el correo) o FIN (terminó el CU).
 */
public class VistaSeguimiento {

    private final String cmUsuarioLogueado;
    private final MapaBolsines mapaBolsines;
    private final List<Integer> bolsinesSinUbicacion;
    private final PosicionBolsin bolsinSeleccionado;
    private final String mensaje;
    private final String paso;

    public VistaSeguimiento(String cmUsuarioLogueado, MapaBolsines mapaBolsines, List<Integer> bolsinesSinUbicacion,
                            PosicionBolsin bolsinSeleccionado, String mensaje, String paso) {
        this.cmUsuarioLogueado = cmUsuarioLogueado;
        this.mapaBolsines = mapaBolsines;
        this.bolsinesSinUbicacion = bolsinesSinUbicacion;
        this.bolsinSeleccionado = bolsinSeleccionado;
        this.mensaje = mensaje;
        this.paso = paso;
    }

    public String getCmUsuarioLogueado() {
        return cmUsuarioLogueado;
    }

    public MapaBolsines getMapaBolsines() {
        return mapaBolsines;
    }

    public List<Integer> getBolsinesSinUbicacion() {
        return bolsinesSinUbicacion;
    }

    public PosicionBolsin getBolsinSeleccionado() {
        return bolsinSeleccionado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getPaso() {
        return paso;
    }
}
