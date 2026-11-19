package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/** <<entity>> Empleado */
@Entity
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apellido;
    private String email;
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    private Rol rol;

    @ManyToOne(fetch = FetchType.EAGER)
    private ComisionMedica asignadoA;

    @OneToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    protected Empleado() {
    }

    public Empleado(String nombre, String apellido, String email, Rol rol,
                    ComisionMedica asignadoA, Usuario usuario) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        this.asignadoA = asignadoA;
        this.usuario = usuario;
    }

    // ---------- Métodos usados en el CU 36 ----------

    public boolean esTuUsuario(Usuario usuario) {
        return usuario.equals(this.usuario);
    }

    public ComisionMedica getCM() {
        return asignadoA;
    }

    /** El empleado resuelve solo si es el Gerente de la CM recibida: revisa su CM y su rol. */
    public boolean sosGCM(ComisionMedica cm) {
        return esTuCM(cm) && rol.esGCM();
    }

    public boolean esTuCM(ComisionMedica cm) {
        return asignadoA.equals(cm);
    }

    public String getEmail() {
        return email;
    }

    // ---------- Soporte para el CU 5 Iniciar sesión ----------

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public Rol getRol() {
        return rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
