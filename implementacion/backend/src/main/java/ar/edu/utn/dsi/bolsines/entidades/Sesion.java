package ar.edu.utn.dsi.bolsines.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

/** <<entity>> Sesion: sesión de un usuario en el sistema. */
@Entity
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;

    protected Sesion() {
    }

    public Sesion(Usuario usuario, LocalDateTime fechaHoraInicio) {
        this.usuario = usuario;
        this.fechaHoraInicio = fechaHoraInicio;
    }

    // ---------- Método usado en el CU 36 ----------

    public Usuario getUsuario() {
        return usuario;
    }

    // ---------- Soporte para el CU 5 Iniciar sesión y el CU 6 Cerrar sesión ----------

    /** La sesión está activa (es la actual) si todavía no tiene fecha y hora de fin. */
    public boolean estaActiva() {
        return fechaHoraFin == null;
    }

    public void cerrar(LocalDateTime fechaHora) {
        this.fechaHoraFin = fechaHora;
    }
}
