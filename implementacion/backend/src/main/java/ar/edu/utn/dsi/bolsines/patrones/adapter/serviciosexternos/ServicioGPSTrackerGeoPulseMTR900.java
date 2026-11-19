package ar.edu.utn.dsi.bolsines.patrones.adapter.serviciosexternos;

import ar.edu.utn.dsi.bolsines.excepciones.GPSTrackerException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * PATRÓN ADAPTER - Adaptable (Adaptee): servicio externo del GPS Tracker modelo GeoPulse MTR-900.
 * Recibe la API Key y el número de bolsín (entero).
 * Devuelve una matriz: cada fila tiene número de bolsín, latitud, longitud y fecha/hora.
 */
public class ServicioGPSTrackerGeoPulseMTR900 {

    private static final String URL_SERVICIO = "http://localhost:8080/api/geopulse-mtr-900";

    private final RestClient restClient = RestClient.builder().baseUrl(URL_SERVICIO).build();

    public Object[][] fetchCargoPositions(String apiKey, int numeroBolsin) {
        try {
            return restClient.get()
                    .uri(uri -> uri.path("/fetchCargoPositions")
                            .queryParam("numeroBolsin", numeroBolsin)
                            .build())
                    .header("X-API-Key", apiKey)
                    .retrieve()
                    .body(Object[][].class);
        } catch (RestClientException e) {
            throw new GPSTrackerException("El GPS Tracker GeoPulse MTR-900 no informó la ubicación del bolsín "
                    + numeroBolsin + " (" + e.getMessage() + ")");
        }
    }
}
