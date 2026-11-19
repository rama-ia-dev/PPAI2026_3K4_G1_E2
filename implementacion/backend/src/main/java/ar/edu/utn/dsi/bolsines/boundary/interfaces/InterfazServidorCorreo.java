package ar.edu.utn.dsi.bolsines.boundary.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// <<boundary>> InterfazServidorCorreo
// Envío de correo simulado: el mail se muestra en la consola del backend.
@Component
public class InterfazServidorCorreo {

    private static final Logger log = LoggerFactory.getLogger(InterfazServidorCorreo.class);

    public void enviarMail(String destinatario, String asunto, String cuerpo) {
        log.info("\n===== MAIL ENVIADO (simulado) =====\nPara: {}\nAsunto: {}\n{}\n===================================",
                destinatario, asunto, cuerpo);
    }
}
