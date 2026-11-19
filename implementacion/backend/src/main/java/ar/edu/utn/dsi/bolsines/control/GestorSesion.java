package ar.edu.utn.dsi.bolsines.control;

import ar.edu.utn.dsi.bolsines.excepciones.FlujoAlternativoException;
import ar.edu.utn.dsi.bolsines.dto.ComisionMedicaDTO;
import ar.edu.utn.dsi.bolsines.dto.SesionDTO;
import ar.edu.utn.dsi.bolsines.entidades.Empleado;
import ar.edu.utn.dsi.bolsines.entidades.Sesion;
import ar.edu.utn.dsi.bolsines.entidades.Usuario;
import ar.edu.utn.dsi.bolsines.repositorios.EmpleadoRepository;
import ar.edu.utn.dsi.bolsines.repositorios.SesionRepository;
import ar.edu.utn.dsi.bolsines.repositorios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// <<control>> GestorSesion - inicio y cierre de sesión (soporte para el CU 36)
@Service
public class GestorSesion {

    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;
    private final EmpleadoRepository empleadoRepository;

    public GestorSesion(UsuarioRepository usuarioRepository, SesionRepository sesionRepository,
                        EmpleadoRepository empleadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.sesionRepository = sesionRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // Valida usuario y contraseña, cierra la sesión anterior y crea una nueva
    public SesionDTO iniciarSesion(String nombreUsuario, String password) {
        Usuario usuario = usuarioRepository.findById(nombreUsuario).orElse(null);
        if (usuario == null || !usuario.esTuPassword(password)) {
            throw new FlujoAlternativoException("LOGIN", "Usuario o contraseña incorrectos.");
        }

        cerrarSesion();
        sesionRepository.save(new Sesion(usuario, LocalDateTime.now()));
        return obtenerSesionActual();
    }

    /** Sesión actual (la que no tiene fecha y hora de fin), o null si no hay usuario logueado. */
    public Sesion getSesionActual() {
        for (Sesion sesion : sesionRepository.findAll()) {
            if (sesion.estaActiva()) {
                return sesion;
            }
        }
        return null;
    }

    // Cierra la sesión activa (si hay una)
    public void cerrarSesion() {
        for (Sesion sesion : sesionRepository.findAll()) {
            if (sesion.estaActiva()) {
                sesion.cerrar(LocalDateTime.now());
                sesionRepository.save(sesion);
            }
        }
    }

    // Devuelve los datos del usuario logueado, o null si no hay sesión
    public SesionDTO obtenerSesionActual() {
        for (Sesion sesion : sesionRepository.findAll()) {
            if (sesion.estaActiva()) {
                Empleado empleado = buscarEmpleado(sesion.getUsuario());
                ComisionMedicaDTO cm = new ComisionMedicaDTO(empleado.getCM().getCodigo(), empleado.getCM().getNombre());
                return new SesionDTO(sesion.getUsuario().getNombre(), empleado.getNombreCompleto(),
                        empleado.getRol().getNombre(), cm);
            }
        }
        return null;
    }

    // Lista los usuarios de prueba para la pantalla de login
    public List<SesionDTO> listarUsuarios() {
        List<SesionDTO> lista = new ArrayList<>();
        for (Empleado empleado : empleadoRepository.findAll()) {
            if (empleado.getUsuario() != null) {
                ComisionMedicaDTO cm = new ComisionMedicaDTO(empleado.getCM().getCodigo(), empleado.getCM().getNombre());
                lista.add(new SesionDTO(empleado.getUsuario().getNombre(), empleado.getNombreCompleto(),
                        empleado.getRol().getNombre(), cm));
            }
        }
        return lista;
    }

    private Empleado buscarEmpleado(Usuario usuario) {
        for (Empleado empleado : empleadoRepository.findAll()) {
            if (empleado.esTuUsuario(usuario)) {
                return empleado;
            }
        }
        throw new FlujoAlternativoException("SESION", "El usuario no tiene un empleado asociado.");
    }
}
