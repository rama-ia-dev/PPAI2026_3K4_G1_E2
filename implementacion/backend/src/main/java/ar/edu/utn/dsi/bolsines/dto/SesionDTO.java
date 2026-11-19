package ar.edu.utn.dsi.bolsines.dto;

// Datos del usuario logueado
public record SesionDTO(String nombreUsuario, String nombreEmpleado, String rol, ComisionMedicaDTO comisionMedica) {
}
