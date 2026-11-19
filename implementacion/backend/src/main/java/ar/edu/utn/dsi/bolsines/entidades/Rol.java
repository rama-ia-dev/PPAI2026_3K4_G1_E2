package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** <<entity>> Rol */
@Entity
public class Rol {

    public static final String GERENTE_CM = "Gerente de Comision Medica";
    public static final String ENCARGADO_BOLSINES = "Encargado de Bolsines";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private String nombre;

    protected Rol() {
    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /** Indica si el rol es Gerente de Comisión Médica. */
    public boolean esGCM() {
        return GERENTE_CM.equals(nombre);
    }

    // ---------- Soporte para el CU 5 Iniciar sesión ----------

    public String getNombre() {
        return nombre;
    }
}
