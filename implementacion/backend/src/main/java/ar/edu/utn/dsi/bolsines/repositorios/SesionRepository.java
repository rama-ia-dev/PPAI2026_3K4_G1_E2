package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Sesion
public interface SesionRepository extends JpaRepository<Sesion, Long> {
}
