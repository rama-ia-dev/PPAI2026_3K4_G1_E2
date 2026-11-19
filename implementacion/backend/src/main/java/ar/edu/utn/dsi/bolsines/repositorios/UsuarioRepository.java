package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Usuario
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
