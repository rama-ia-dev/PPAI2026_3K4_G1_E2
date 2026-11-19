package ar.edu.utn.dsi.bolsines.repositorios;

import ar.edu.utn.dsi.bolsines.entidades.Bolsin;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a base de datos de Bolsin
public interface BolsinRepository extends JpaRepository<Bolsin, Integer> {
}
