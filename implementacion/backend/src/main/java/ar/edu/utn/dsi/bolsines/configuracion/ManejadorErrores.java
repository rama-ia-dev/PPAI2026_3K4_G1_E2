package ar.edu.utn.dsi.bolsines.configuracion;

import ar.edu.utn.dsi.bolsines.excepciones.FlujoAlternativoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Convierte los flujos alternativos en una respuesta para el frontend: { flujo, mensaje }
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(FlujoAlternativoException.class)
    public ResponseEntity<Map<String, String>> flujoAlternativo(FlujoAlternativoException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("flujo", e.getFlujo(), "mensaje", e.getMessage()));
    }
}
