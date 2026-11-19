package ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * PATRÓN ADAPTER - Adaptable (Adaptee): servicio externo del GPS Tracker modelo XTR-4500L.
 * Recibe la API Key, el número de bolsín (entero) y el código de la CM origen (String).
 * Devuelve un JSON en formato cadena de caracteres.
 */
public class ServicioGPSTrackerXTR4500L {

    private static final String URL_SERVICIO = "http://localhost:8080/api/xtr-4500l";

    private final RestClient restClient = RestClient.builder().baseUrl(URL_SERVICIO).build();

    public String getBolsinLocation(String apiKey, int numeroBolsin, String codigoCMOrigen) {
        try {
            return restClient.get()
                    .uri(uri -> uri.path("/getBolsinLocation")
                            .queryParam("numeroBolsin", numeroBolsin)
                            .queryParam("codigoComisionMedicaOrigen", codigoCMOrigen)
                            .build())
                    .header("X-API-Key", apiKey)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new GPSTrackerException("El GPS Tracker XTR-4500L no informó la ubicación del bolsín "
                    + numeroBolsin + " (" + e.getMessage() + ")");
        }
    }
}
