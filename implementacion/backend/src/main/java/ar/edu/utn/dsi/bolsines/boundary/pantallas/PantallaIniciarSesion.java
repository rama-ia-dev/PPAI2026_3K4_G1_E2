package ar.edu.utn.dsi.bolsines.boundary.pantallas;

import ar.edu.utn.dsi.bolsines.control.GestorSesion;
import ar.edu.utn.dsi.bolsines.dto.LoginRequest;
import ar.edu.utn.dsi.bolsines.dto.SesionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// <<boundary>> PantallaIniciarSesion
@RestController
@RequestMapping("/api/sesion")
public class PantallaIniciarSesion {

    private final GestorSesion gestorSesion;

    public PantallaIniciarSesion(GestorSesion gestorSesion) {
        this.gestorSesion = gestorSesion;
    }

    @PostMapping("/iniciar")
    public SesionDTO iniciarSesion(@RequestBody LoginRequest login) {
        return gestorSesion.iniciarSesion(login.nombreUsuario(), login.password());
    }

    @PostMapping("/cerrar")
    public Map<String, String> cerrarSesion() {
        gestorSesion.cerrarSesion();
        return Map.of("mensaje", "Sesión cerrada.");
    }

    // Devuelve el usuario logueado (204 si no hay sesión)
    @GetMapping("/actual")
    public ResponseEntity<SesionDTO> sesionActual() {
        SesionDTO sesion = gestorSesion.obtenerSesionActual();
        if (sesion == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sesion);
    }

    @GetMapping("/usuarios")
    public List<SesionDTO> usuarios() {
        return gestorSesion.listarUsuarios();
    }
}
