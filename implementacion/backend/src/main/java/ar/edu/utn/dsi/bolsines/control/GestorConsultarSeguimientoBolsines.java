package ar.edu.utn.dsi.bolsines.control;

import ar.edu.utn.dsi.bolsines.boundary.interfaces.InterfazGPSTracker;
import ar.edu.utn.dsi.bolsines.boundary.interfaces.InterfazGoogleMaps;
import ar.edu.utn.dsi.bolsines.boundary.pantallas.PantallaConsultarSeguimientoBolsines;
import ar.edu.utn.dsi.bolsines.dto.MapaBolsines;
import ar.edu.utn.dsi.bolsines.dto.PosicionBolsin;
import ar.edu.utn.dsi.bolsines.entidades.Bolsin;
import ar.edu.utn.dsi.bolsines.entidades.ComisionMedica;
import ar.edu.utn.dsi.bolsines.entidades.Empleado;
import ar.edu.utn.dsi.bolsines.entidades.Sesion;
import ar.edu.utn.dsi.bolsines.entidades.Usuario;
import ar.edu.utn.dsi.bolsines.excepciones.FlujoAlternativoException;
import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import ar.edu.utn.dsi.bolsines.patrones.adapter.UbicacionBolsin;
import ar.edu.utn.dsi.bolsines.repositorios.BolsinRepository;
import ar.edu.utn.dsi.bolsines.repositorios.EmpleadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <<control>> GestorConsultarSeguimientoBolsines - CU 36 Consultar seguimiento de bolsines.
 * Cada método sigue, mensaje por mensaje y en el mismo orden, los diagramas de secuencia de diseño
 * (Figuras 3, 4 y 5 de documento/PPAI2026_3K4_G1_E2_Diseño.pdf). Los repositorios son el acceso a la base de datos para recorrer
 * "todos los empleados" y "todos los bolsines" de los loops del diagrama.
 */
@Service
public class GestorConsultarSeguimientoBolsines {

    private static final Logger log = LoggerFactory.getLogger(GestorConsultarSeguimientoBolsines.class);

    // ---------- Colaboradores ----------
    private final PantallaConsultarSeguimientoBolsines pantalla;
    private final InterfazGPSTracker interfazGPSTracker;
    private final InterfazGoogleMaps interfazGoogleMaps;
    private final GestorNotificarUbicacionBolsin gestorNotificarUbicacionBolsin;
    private final GestorSesion gestorSesion;
    private final EmpleadoRepository empleadoRepository;
    private final BolsinRepository bolsinRepository;

    // ---------- Atributos del diagrama de clases de diseño ----------
    private ComisionMedica cmUsuarioLogueado;
    private List<Bolsin> bolsinesEnviados = new ArrayList<>();
    private List<PosicionBolsin> posicionesBolsines = new ArrayList<>();
    private List<Integer> bolsinesSinUbicacion = new ArrayList<>();
    private Bolsin bolsinSeleccionado;
    private PosicionBolsin posicionBolsinSeleccionado;
    private String mailGerenteCMDestino;

    public GestorConsultarSeguimientoBolsines(@Lazy PantallaConsultarSeguimientoBolsines pantalla,
                                              InterfazGPSTracker interfazGPSTracker,
                                              InterfazGoogleMaps interfazGoogleMaps,
                                              GestorNotificarUbicacionBolsin gestorNotificarUbicacionBolsin,
                                              GestorSesion gestorSesion,
                                              EmpleadoRepository empleadoRepository,
                                              BolsinRepository bolsinRepository) {
        this.pantalla = pantalla;
        this.interfazGPSTracker = interfazGPSTracker;
        this.interfazGoogleMaps = interfazGoogleMaps;
        this.gestorNotificarUbicacionBolsin = gestorNotificarUbicacionBolsin;
        this.gestorSesion = gestorSesion;
        this.empleadoRepository = empleadoRepository;
        this.bolsinRepository = bolsinRepository;
    }

    // =====================================================================================
    // Pasos 2 a 6: buscar la CM del usuario, los bolsines enviados y su ubicación, y armar el mapa
    // =====================================================================================

    public synchronized void consultarSeguimientoBolsines() {
        buscarCMUsuarioLogueado();
        pantalla.mostrarCMUsuarioLogueado(cmUsuarioLogueado.getNombre());

        buscarBolsinesEnviados();
        if (bolsinesEnviados.isEmpty()) {
            // A1: no se encuentran bolsines en estado Enviado
            pantalla.informarSinBolsinesEnviados();
            finCU();
            return;
        }

        obtenerUbicacionBolsines();
        if (posicionesBolsines.isEmpty()) {
            // A3: el GPS Tracker no pudo informar la ubicación de ningún bolsín
            pantalla.informarBolsinesSinUbicacion(bolsinesSinUbicacion);
            finCU();
            return;
        }

        armarMapaDePosicionDeBolsines();
        if (!bolsinesSinUbicacion.isEmpty()) {
            // A3: algunos bolsines no tienen ubicación; los demás se muestran en el mapa
            pantalla.informarBolsinesSinUbicacion(bolsinesSinUbicacion);
        }
    }

    /** Paso 2: la CM del usuario logueado sale de la sesión actual. */
    private void buscarCMUsuarioLogueado() {
        Sesion actual = gestorSesion.getSesionActual();
        if (actual == null) {
            throw new FlujoAlternativoException("SESION", "No hay un usuario logueado.");
        }
        Usuario usuario = actual.getUsuario();
        Empleado logueado = null;
        for (Empleado empleado : empleadoRepository.findAll()) {
            if (empleado.esTuUsuario(usuario)) {
                logueado = empleado;
            }
        }
        if (logueado == null) {
            throw new FlujoAlternativoException("SESION", "El usuario logueado no tiene un empleado asociado.");
        }
        cmUsuarioLogueado = logueado.getCM();
    }

    /** Paso 3: bolsines en estado Enviado cuya CM origen es la del usuario logueado. */
    private void buscarBolsinesEnviados() {
        bolsinesEnviados = new ArrayList<>();
        for (Bolsin bolsin : bolsinRepository.findAll()) {
            if (bolsin.esTuCMOrigen(cmUsuarioLogueado) && bolsin.sosEnviado()) {
                bolsinesEnviados.add(bolsin);
            }
        }
    }

    /** Pasos 4 y 5: pide a la InterfazGPSTracker la ubicación de cada bolsín (patrón Adapter). */
    private void obtenerUbicacionBolsines() {
        posicionesBolsines = new ArrayList<>();
        bolsinesSinUbicacion = new ArrayList<>();
        for (Bolsin bolsin : bolsinesEnviados) {
            int numeroBolsin = bolsin.getNumeroBolsin();
            String modeloGPSTracker = bolsin.getModeloGPSTracker();
            ComisionMedica cmDestino = bolsin.obtenerCMDestino();
            String codigoCMOrigen = cmUsuarioLogueado.getCodigo();
            String codigoCMDestino = cmDestino.getCodigo();
            try {
                UbicacionBolsin ubicacion = interfazGPSTracker.obtenerUbicacionBolsin(
                        numeroBolsin, codigoCMOrigen, codigoCMDestino, modeloGPSTracker);
                int numeroPrecinto = bolsin.getNroPrecinto();
                String nombreCMDestino = cmDestino.getNombre();
                double latitud = ubicacion.getLatitud();
                double longitud = ubicacion.getLongitud();
                posicionesBolsines.add(new PosicionBolsin(numeroBolsin, numeroPrecinto, codigoCMDestino,
                        nombreCMDestino, modeloGPSTracker, latitud, longitud,
                        ubicacion.getFechaHoraUltimaActualizacion()));
            } catch (GPSTrackerException e) {
                log.warn("A3 - {}", e.getMessage());
                bolsinesSinUbicacion.add(numeroBolsin);
            }
        }
    }

    /** Paso 6: arma el mapa con la posición de los bolsines y se lo pasa a la pantalla. */
    private void armarMapaDePosicionDeBolsines() {
        MapaBolsines mapa = interfazGoogleMaps.obtenerMapa(posicionesBolsines);
        pantalla.mostrarBolsinesEnMapa(mapa);
    }

    /** Paso 6 (opción de filtrar): por número de precinto y/o CM destino. A2 si el precinto no existe. */
    public synchronized void tomarFiltro(Integer numeroPrecinto, String codigoCMDestino) {
        List<PosicionBolsin> filtradas = new ArrayList<>();
        boolean existePrecinto = false;
        for (PosicionBolsin posicion : posicionesBolsines) {
            boolean coincidePrecinto = numeroPrecinto == null || posicion.getNumeroPrecinto() == numeroPrecinto;
            boolean coincideDestino = codigoCMDestino == null || codigoCMDestino.isBlank()
                    || posicion.getCodigoCMDestino().equals(codigoCMDestino);
            if (coincidePrecinto) {
                existePrecinto = true;
            }
            if (coincidePrecinto && coincideDestino) {
                filtradas.add(posicion);
            }
        }
        if (numeroPrecinto != null && !existePrecinto) {
            // A2: no se encuentra bolsín con el número de precinto ingresado
            pantalla.informarPrecintoInexistente(numeroPrecinto);
            return;
        }
        if (filtradas.isEmpty()) {
            // El precinto existe, pero no va a la CM destino elegida
            pantalla.informarSinCoincidenciasFiltro();
            return;
        }
        pantalla.mostrarBolsinesEnMapa(interfazGoogleMaps.obtenerMapa(filtradas));
    }

    // =====================================================================================
    // Pasos 7 a 11: selección del bolsín, confirmación y notificación al Gerente de la CM destino
    // =====================================================================================

    /** Paso 7: el EB selecciona un bolsín del mapa; paso 8: se pide confirmar el envío del correo. */
    public synchronized void tomarSeleccionBolsin(int numeroBolsin) {
        bolsinSeleccionado = null;
        for (Bolsin bolsin : bolsinesEnviados) {
            if (bolsin.getNumeroBolsin() == numeroBolsin) {
                bolsinSeleccionado = bolsin;
            }
        }
        posicionBolsinSeleccionado = buscarPosicionBolsin(numeroBolsin);
        if (bolsinSeleccionado == null || posicionBolsinSeleccionado == null) {
            throw new FlujoAlternativoException("SELECCION", "El bolsín " + numeroBolsin + " no está en el mapa.");
        }
        pantalla.solicitarConfirmacionEnvioMail(posicionBolsinSeleccionado);
    }

    private PosicionBolsin buscarPosicionBolsin(int numeroBolsin) {
        for (PosicionBolsin posicion : posicionesBolsines) {
            if (posicion.getNumeroBolsin() == numeroBolsin) {
                return posicion;
            }
        }
        return null;
    }

    /** Paso 9: el EB confirma el envío (A5 si no lo confirma). Pasos 10 y 11. */
    public synchronized void tomarConfirmacionEnvioMail(boolean confirma) {
        if (bolsinSeleccionado == null) {
            throw new FlujoAlternativoException("SELECCION", "Primero debe seleccionar un bolsín del mapa.");
        }
        if (!confirma) {
            // A5: el EB no selecciona la opción para enviar el correo
            pantalla.informarMailNoEnviado();
            finCU();
            return;
        }
        buscarMailGerenteCMDestino();
        llamarCUNotificarUbicacionBolsin();
        pantalla.informarEnvioExitoso(mailGerenteCMDestino);
        finCU();
    }

    /** Paso 10: busca el correo del Gerente de la CM destino del bolsín seleccionado. */
    private void buscarMailGerenteCMDestino() {
        ComisionMedica cmDestino = bolsinSeleccionado.obtenerCMDestino();
        Empleado gerente = null;
        for (Empleado empleado : empleadoRepository.findAll()) {
            if (empleado.sosGCM(cmDestino)) {
                gerente = empleado;
            }
        }
        if (gerente == null) {
            throw new FlujoAlternativoException("GCM", "La CM destino no tiene un Gerente registrado.");
        }
        mailGerenteCMDestino = gerente.getEmail();
    }

    /** Paso 11: <<include>> CU 31 Notificar ubicación de bolsín. */
    private void llamarCUNotificarUbicacionBolsin() {
        gestorNotificarUbicacionBolsin.notificarUbicacionBolsin(
                mailGerenteCMDestino,
                posicionBolsinSeleccionado.getNumeroBolsin(),
                posicionBolsinSeleccionado.getFechaHoraUltimaActualizacion(),
                posicionBolsinSeleccionado.getLatitud(),
                posicionBolsinSeleccionado.getLongitud());
    }

    /** A4: el EB cancela la ejecución del caso de uso. */
    public synchronized void cancelarCU() {
        finCU();
    }

    private void finCU() {
        cmUsuarioLogueado = null;
        bolsinesEnviados = new ArrayList<>();
        posicionesBolsines = new ArrayList<>();
        bolsinesSinUbicacion = new ArrayList<>();
        bolsinSeleccionado = null;
        posicionBolsinSeleccionado = null;
        mailGerenteCMDestino = null;
    }
}
