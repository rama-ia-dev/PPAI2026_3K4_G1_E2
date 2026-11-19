package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Rol
public interface RolRepository extends JpaRepository<Rol, Long> {
}
