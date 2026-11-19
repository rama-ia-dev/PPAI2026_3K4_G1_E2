package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Empleado
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
