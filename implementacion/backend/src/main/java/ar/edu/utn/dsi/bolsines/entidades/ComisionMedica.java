package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * <<entity>> ComisionMedica
 * Dos objetos ComisionMedica son la misma comisión si tienen el mismo código (equals).
 */
@Entity
public class ComisionMedica {

    @Id
    private String codigo; // ej: CM-CBA-01
    private String direccion;
    private String email;
    private String nombre;
    private String telefono;

    protected ComisionMedica() {
    }

    public ComisionMedica(String codigo, String nombre, String direccion, String email, String telefono) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof ComisionMedica cm && Objects.equals(codigo, cm.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }
}
