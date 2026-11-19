package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Estado
public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
