package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.ComisionMedica;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de ComisionMedica
public interface ComisionMedicaRepository extends JpaRepository<ComisionMedica, String> {
}
