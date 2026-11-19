package ar.edu.utn.dsi.bolsines.boundary.pantallas;

import ar.edu.utn.dsi.bolsines.control.GestorConsultarSeguimientoBolsines;
import ar.edu.utn.dsi.bolsines.dto.MapaBolsines;
import ar.edu.utn.dsi.bolsines.dto.PosicionBolsin;
import ar.edu.utn.dsi.bolsines.dto.VistaSeguimiento;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <<boundary>> PantallaConsultarSeguimientoBolsines
 * La parte visual corre en el navegador (React). Cada acción del EB llega a uno de los métodos
 * opcion...() / tomar...() de esta clase, con los mismos parámetros del diagrama, y la pantalla
 * devuelve su estado actual (VistaSeguimiento) para que el navegador lo dibuje.
 * Los mensajes que le manda el gestor (mostrar..., informar..., solicitar...) actualizan ese estado.
 */
@RestController
@RequestMapping("/api/seguimiento-bolsines")
public class PantallaConsultarSeguimientoBolsines {

    private final GestorConsultarSeguimientoBolsines gestor;

    // ---------- Atributos del diagrama de clases de diseño ----------
    private String cmUsuarioLogueado;
    private MapaBolsines mapaBolsines;
    private List<Integer> bolsinesSinUbicacion = new ArrayList<>();
    private PosicionBolsin bolsinSeleccionado;
    private String mensaje;
    private String paso;

    public PantallaConsultarSeguimientoBolsines(GestorConsultarSeguimientoBolsines gestor) {
        this.gestor = gestor;
    }

    // =====================================================================================
    // Acciones del EB (llegan desde el navegador)
    // =====================================================================================

    /** Paso 1: el EB selecciona la opción para consultar la ubicación de los bolsines. */
    @PostMapping("/opcion")
    public synchronized VistaSeguimiento opcionConsultarSeguimientoBolsines() {
        habilitarPantalla();
        gestor.consultarSeguimientoBolsines();
        return armarVista();
    }

    /** Paso 6: el EB filtra por número de precinto y/o CM destino. */
    @PostMapping("/filtro")
    public synchronized VistaSeguimiento tomarFiltro(@RequestParam(required = false) Integer numeroPrecinto,
                                                     @RequestParam(required = false) String codigoCMDestino) {
        mensaje = null;
        gestor.tomarFiltro(numeroPrecinto, codigoCMDestino);
        return armarVista();
    }

    /** Paso 7: el EB selecciona un bolsín del mapa. */
    @PostMapping("/seleccion")
    public synchronized VistaSeguimiento tomarSeleccionBolsin(@RequestParam int numeroBolsin) {
        gestor.tomarSeleccionBolsin(numeroBolsin);
        return armarVista();
    }

    /** Paso 9 (o A5): el EB confirma, o no, el envío del correo. */
    @PostMapping("/confirmacion")
    public synchronized VistaSeguimiento tomarConfirmacionEnvioMail(@RequestParam boolean confirma) {
        gestor.tomarConfirmacionEnvioMail(confirma);
        return armarVista();
    }

    /** A4: el EB cancela el caso de uso. */
    @PostMapping("/cancelacion")
    public synchronized VistaSeguimiento tomarCancelacion() {
        gestor.cancelarCU();
        mensaje = "Se canceló la consulta de seguimiento de bolsines.";
        paso = "FIN";
        return armarVista();
    }

    // =====================================================================================
    // Mensajes de la propia pantalla y del gestor
    // =====================================================================================

    public void habilitarPantalla() {
        cmUsuarioLogueado = null;
        mapaBolsines = null;
        bolsinesSinUbicacion = new ArrayList<>();
        bolsinSeleccionado = null;
        mensaje = null;
        paso = "INICIO";
    }

    public void mostrarCMUsuarioLogueado(String nombreCM) {
        cmUsuarioLogueado = nombreCM;
    }

    public void mostrarBolsinesEnMapa(MapaBolsines mapa) {
        mapaBolsines = mapa;
        solicitarSeleccionBolsin();
    }

    public void solicitarSeleccionBolsin() {
        bolsinSeleccionado = null;
        paso = "SELECCION";
    }

    public void informarSinBolsinesEnviados() {
        mensaje = "No se encontraron bolsines en estado Enviado para " + cmUsuarioLogueado + ".";
        paso = "FIN";
    }

    public void informarBolsinesSinUbicacion(List<Integer> numerosBolsin) {
        bolsinesSinUbicacion = new ArrayList<>(numerosBolsin);
        if (mapaBolsines == null) {
            mensaje = "El GPS Tracker no pudo informar la ubicación de ningún bolsín.";
            paso = "FIN";
        } else {
            List<String> numeros = numerosBolsin.stream().map(String::valueOf).toList();
            String bolsines = numeros.size() == 1
                    ? "del bolsín " + numeros.get(0)
                    : "de los bolsines " + String.join(", ", numeros.subList(0, numeros.size() - 1))
                      + " y " + numeros.get(numeros.size() - 1);
            mensaje = "El GPS Tracker no pudo informar la ubicación " + bolsines + ".";
        }
    }

    public void informarPrecintoInexistente(Integer numeroPrecinto) {
        mensaje = "No se encontró un bolsín con el número de precinto " + numeroPrecinto + ".";
    }

    public void informarSinCoincidenciasFiltro() {
        mensaje = "Ningún bolsín cumple a la vez con el precinto y la CM destino elegidos.";
    }

    public void solicitarConfirmacionEnvioMail(PosicionBolsin posicion) {
        bolsinSeleccionado = posicion;
        paso = "CONFIRMACION";
    }

    public void informarEnvioExitoso(String mailGerente) {
        mensaje = "Se notificó la ubicación del bolsín " + bolsinSeleccionado.getNumeroBolsin()
                + " al Gerente de " + bolsinSeleccionado.getNombreCMDestino() + " (" + mailGerente + ").";
        paso = "FIN";
    }

    public void informarMailNoEnviado() {
        mensaje = "No se envió el correo electrónico. Fin de la consulta.";
        paso = "FIN";
    }

    private VistaSeguimiento armarVista() {
        return new VistaSeguimiento(cmUsuarioLogueado, mapaBolsines, bolsinesSinUbicacion,
                bolsinSeleccionado, mensaje, paso);
    }
}
