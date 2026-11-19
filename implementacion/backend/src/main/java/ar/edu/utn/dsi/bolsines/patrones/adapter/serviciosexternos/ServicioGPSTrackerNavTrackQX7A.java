package ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * PATRÓN ADAPTER - Adaptable (Adaptee): servicio externo del GPS Tracker modelo NavTrack QX-7A.
 * Recibe la API Key, el número de bolsín (entero) y el código de la CM destino (String).
 * Devuelve un único String con sus ítems separados por comas.
 */
public class ServicioGPSTrackerNavTrackQX7A {

    private static final String URL_SERVICIO = "http://localhost:8080/api/navtrack-qx-7a";

    private final RestClient restClient = RestClient.builder().baseUrl(URL_SERVICIO).build();

    public String retrieveTrackingData(String apiKey, int numeroBolsin, String codigoCMDestino) {
        try {
            return restClient.get()
                    .uri(uri -> uri.path("/retrieveTrackingData")
                            .queryParam("numeroBolsin", numeroBolsin)
                            .queryParam("codigoComisionMedicaDestino", codigoCMDestino)
                            .build())
                    .header("X-API-Key", apiKey)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            throw new GPSTrackerException("El GPS Tracker NavTrack QX-7A no informó la ubicación del bolsín "
                    + numeroBolsin + " (" + e.getMessage() + ")");
        }
    }
}
