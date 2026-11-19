package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

/**
 * <<entity>> Usuario
 * Dos objetos Usuario son el mismo usuario si tienen el mismo nombre (equals).
 */
@Entity
public class Usuario {

    @Id
    private String nombre;
    private String password;

    protected Usuario() {
    }

    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }

    // ---------- Soporte para el CU 5 Iniciar sesión ----------

    public boolean esTuPassword(String passwordIngresada) {
        return password.equals(passwordIngresada);
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        return otro instanceof Usuario u && Objects.equals(nombre, u.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }
}
