package ar.edu.utn.dsi.bolsines.control;

import ar.edu.utn.dsi.bolsines.boundary.interfaces.InterfazServidorCorreo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <<control>> GestorNotificarUbicacionBolsin - CU 31 Notificar ubicación de bolsín
 * (incluido por el CU 36). Arma el correo con número de bolsín, fecha y hora de la última
 * actualización y coordenadas, y lo envía por la InterfazServidorCorreo.
 */
@Service
public class GestorNotificarUbicacionBolsin {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final InterfazServidorCorreo interfazServidorCorreo;

    public GestorNotificarUbicacionBolsin(InterfazServidorCorreo interfazServidorCorreo) {
        this.interfazServidorCorreo = interfazServidorCorreo;
    }

    public void notificarUbicacionBolsin(String mailGerente, int numeroBolsin,
                                         LocalDateTime fechaHoraUltimaActualizacion,
                                         double latitud, double longitud) {
        String asunto = "Ubicación del bolsín N° " + numeroBolsin;
        String cuerpo = "Le informamos la ubicación del bolsín que se dirige a su Comisión Médica.\n"
                + "Número de bolsín: " + numeroBolsin + "\n"
                + "Fecha y hora de última actualización: " + fechaHoraUltimaActualizacion.format(FORMATO) + "\n"
                + "Coordenadas: " + latitud + ", " + longitud + "\n"
                + "Mapa: https://www.openstreetmap.org/?mlat=" + latitud + "&mlon=" + longitud;
        interfazServidorCorreo.enviarMail(mailGerente, asunto, cuerpo);
    }
}
