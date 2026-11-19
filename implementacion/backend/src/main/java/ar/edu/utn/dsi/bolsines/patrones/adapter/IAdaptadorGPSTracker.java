package ar.edu.utn.dsi.bolsines.patrones.adapter;

/**
 * PATRÓN ADAPTER - Objetivo (Target).
 * Interfaz común que conoce el cliente (InterfazGPSTracker) y que implementa
 * el adaptador de cada modelo de GPS Tracker. Sus tres métodos son polimórficos.
 */
public interface IAdaptadorGPSTracker {

    /** Devuelve la ubicación del bolsín en el formato común (UbicacionBolsin). */
    UbicacionBolsin obtenerUbicacionBolsin(int numeroBolsin, String codigoCMOrigen, String codigoCMDestino);

    /** Pide la ubicación al servicio externo del modelo, con los parámetros que ese modelo necesita. */
    void solicitarUbicacionBolsin(int numeroBolsin, String codigoCMOrigen, String codigoCMDestino);

    /** Traduce la respuesta del servicio externo al formato común UbicacionBolsin. */
    void adaptarUbicacionBolsin();
}
