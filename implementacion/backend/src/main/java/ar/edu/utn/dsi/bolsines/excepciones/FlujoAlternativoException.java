package ar.edu.utn.dsi.bolsines.excepciones;

// Excepción para cortar el flujo cuando ocurre un flujo alternativo del CU (A1, A2, ...)
public class FlujoAlternativoException extends RuntimeException {

    private final String flujo;

    public FlujoAlternativoException(String flujo, String mensaje) {
        super(mensaje);
        this.flujo = flujo;
    }

    public String getFlujo() {
        return flujo;
    }
}
