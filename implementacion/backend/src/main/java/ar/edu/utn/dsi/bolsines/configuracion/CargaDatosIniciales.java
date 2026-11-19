package ar.edu.utn.dsi.bolsines.configuracion;

import ar.edu.utn.dsi.bolsines.boundary.interfaces.InterfazGPSTracker;
import ar.edu.utn.dsi.bolsines.entidades.Bolsin;
import ar.edu.utn.dsi.bolsines.entidades.ComisionMedica;
import ar.edu.utn.dsi.bolsines.entidades.Empleado;
import ar.edu.utn.dsi.bolsines.entidades.Estado;
import ar.edu.utn.dsi.bolsines.entidades.Rol;
import ar.edu.utn.dsi.bolsines.entidades.Usuario;
import ar.edu.utn.dsi.bolsines.repositorios.BolsinRepository;
import ar.edu.utn.dsi.bolsines.repositorios.ComisionMedicaRepository;
import ar.edu.utn.dsi.bolsines.repositorios.EmpleadoRepository;
import ar.edu.utn.dsi.bolsines.repositorios.EstadoRepository;
import ar.edu.utn.dsi.bolsines.repositorios.RolRepository;
import ar.edu.utn.dsi.bolsines.repositorios.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Carga los datos de prueba al iniciar el sistema (la base se recrea vacía en cada arranque).
// Los números de bolsín y los códigos de CM coinciden con los que conoce la API simulada del GPS Tracker
// (implementacion/api-gps-tracker).
@Component
public class CargaDatosIniciales implements CommandLineRunner {

    private static final String XTR = InterfazGPSTracker.MODELO_XTR_4500L;
    private static final String NAVTRACK = InterfazGPSTracker.MODELO_NAVTRACK_QX_7A;
    private static final String GEOPULSE = InterfazGPSTracker.MODELO_GEOPULSE_MTR_900;

    private final EstadoRepository estadoRepository;
    private final RolRepository rolRepository;
    private final ComisionMedicaRepository comisionMedicaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    private final BolsinRepository bolsinRepository;

    // Estados del bolsín
    private Estado creado;
    private Estado cerrado;
    private Estado enviado;
    private Estado recibidoEnCMDestino;

    public CargaDatosIniciales(EstadoRepository estadoRepository, RolRepository rolRepository,
                               ComisionMedicaRepository comisionMedicaRepository, UsuarioRepository usuarioRepository,
                               EmpleadoRepository empleadoRepository, BolsinRepository bolsinRepository) {
        this.estadoRepository = estadoRepository;
        this.rolRepository = rolRepository;
        this.comisionMedicaRepository = comisionMedicaRepository;
        this.usuarioRepository = usuarioRepository;
        this.empleadoRepository = empleadoRepository;
        this.bolsinRepository = bolsinRepository;
    }

    @Override
    public void run(String... args) {
        if (estadoRepository.count() > 0) {
            return; // ya hay datos cargados
        }

        // ---------- Estados ----------
        creado = estadoRepository.save(new Estado("Bolsin", "Creado", "Bolsín registrado"));
        cerrado = estadoRepository.save(new Estado("Bolsin", "Cerrado", "Bolsín cerrado con precinto"));
        enviado = estadoRepository.save(new Estado("Bolsin", "Enviado", "Bolsín retirado por el correo"));
        recibidoEnCMDestino = estadoRepository.save(new Estado("Bolsin", "RecibidoEnCMDestino", "Bolsín recibido"));
        estadoRepository.save(new Estado("Bolsin", "DeBaja", "Bolsín eliminado"));
        estadoRepository.save(new Estado("Remito", "Creado", "Remito generado"));
        estadoRepository.save(new Estado("Remito", "EnBolsinSaliente", "Remito agregado a un bolsín"));
        estadoRepository.save(new Estado("Remito", "EnBolsinEnviado", "Remito en un bolsín enviado"));

        // ---------- Roles ----------
        Rol eb = rolRepository.save(new Rol(Rol.ENCARGADO_BOLSINES, "Gestiona bolsines"));
        Rol gcm = rolRepository.save(new Rol(Rol.GERENTE_CM, "Gerente de la comisión médica"));

        // ---------- Comisiones médicas ----------
        ComisionMedica cordoba = cm("CM-CBA-01", "Comisión Médica Córdoba", "Av. Colón 1200, Córdoba");
        ComisionMedica villaMaria = cm("CM-CBA-02", "Comisión Médica Villa María", "San Martín 450, Villa María");
        ComisionMedica rosario = cm("CM-ROS-02", "Comisión Médica Rosario", "Bv. Oroño 900, Rosario");
        ComisionMedica mendoza = cm("CM-MDZ-03", "Comisión Médica Mendoza", "Av. San Martín 1500, Mendoza");
        ComisionMedica central = cm("CM-BA-01", "Comisión Médica Central", "Av. de Mayo 700, C.A.B.A.");
        ComisionMedica tucuman = cm("CM-TUC-01", "Comisión Médica Tucumán", "24 de Septiembre 600, Tucumán");
        ComisionMedica salta = cm("CM-SLA-01", "Comisión Médica Salta", "Belgrano 1100, Salta");
        ComisionMedica jujuy = cm("CM-JUJ-01", "Comisión Médica Jujuy", "Belgrano 300, San Salvador de Jujuy");
        ComisionMedica neuquen = cm("CM-NQN-01", "Comisión Médica Neuquén", "Argentina 200, Neuquén");
        cm("CM-MDQ-01", "Comisión Médica Mar del Plata", "Independencia 2100, Mar del Plata");
        cm("CM-LPL-01", "Comisión Médica La Plata", "Calle 7 N° 800, La Plata");

        // ---------- Encargados de bolsines (los que tienen nombre de usuario pueden iniciar sesión) ----------
        Empleado ebCordoba = empleado("Lucía", "Fernández", "lfernandez@bolsines.example", eb, cordoba, "eb.cordoba");
        Empleado ebRosario = empleado("Martín", "Gómez", "mgomez@bolsines.example", eb, rosario, "eb.rosario");
        empleado("Paula", "Quispe", "pquispe@bolsines.example", eb, jujuy, "eb.jujuy");
        Empleado ebCentral = empleado("Sofía", "Luna", "sluna@bolsines.example", eb, central, null);

        // ---------- Gerentes de comisión médica (destinatarios del mail) ----------
        empleado("Carlos", "Medina", "gcm.cordoba@bolsines.example", gcm, cordoba, null);
        empleado("Silvina", "Rossi", "gcm.rosario@bolsines.example", gcm, rosario, null);
        empleado("Jorge", "Aguirre", "gcm.mendoza@bolsines.example", gcm, mendoza, null);
        empleado("Laura", "Benítez", "gcm.central@bolsines.example", gcm, central, null);
        empleado("Ricardo", "Paz", "gcm.tucuman@bolsines.example", gcm, tucuman, null);
        empleado("Andrea", "Cruz", "gcm.salta@bolsines.example", gcm, salta, null);
        empleado("Diego", "Sosa", "gcm.neuquen@bolsines.example", gcm, neuquen, null);
        empleado("Mónica", "Ruiz", "gcm.villamaria@bolsines.example", gcm, villaMaria, null);

        // ---------- Bolsines enviados desde Córdoba (aparecen en el mapa) ----------
        // La API simulada del GPS Tracker conoce los bolsines 101 a 110. Cada modelo valida distinto:
        // XTR-4500L controla la CM origen, NavTrack QX-7A la CM destino y GeoPulse MTR-900 solo el número.
        bolsinEnviado(101, 50101, XTR, cordoba, rosario, ebCordoba);       // XTR: origen CM-CBA-01 en la API
        bolsinEnviado(103, 50103, NAVTRACK, cordoba, central, ebCordoba);  // NavTrack: destino CM-BA-01 en la API
        bolsinEnviado(106, 50106, NAVTRACK, cordoba, jujuy, ebCordoba);    // NavTrack: destino CM-JUJ-01 en la API
        bolsinEnviado(105, 50105, GEOPULSE, cordoba, salta, ebCordoba);    // GeoPulse: solo número
        bolsinEnviado(108, 50108, GEOPULSE, cordoba, tucuman, ebCordoba);  // GeoPulse: solo número

        // A3: bolsín enviado que el GPS Tracker no conoce (no está en la API simulada)
        bolsinEnviado(116, 50116, XTR, cordoba, neuquen, ebCordoba);

        // ---------- Bolsines de Córdoba en otros estados (NO aparecen en el mapa) ----------
        Bolsin b117 = new Bolsin(117, LocalDate.of(2026, 3, 7), 0, 900, XTR, cordoba, rosario);
        b117.cambiarEstado(creado, ebCordoba, LocalDateTime.of(2026, 3, 7, 9, 0));
        bolsinRepository.save(b117);

        Bolsin b118 = new Bolsin(118, LocalDate.of(2026, 3, 7), 50118, 1500, NAVTRACK, cordoba, mendoza);
        b118.cambiarEstado(creado, ebCordoba, LocalDateTime.of(2026, 3, 7, 9, 10));
        b118.cambiarEstado(cerrado, ebCordoba, LocalDateTime.of(2026, 3, 7, 9, 40));
        bolsinRepository.save(b118);

        Bolsin b119 = new Bolsin(119, LocalDate.of(2026, 3, 2), 50119, 1100, GEOPULSE, cordoba, central);
        b119.cambiarEstado(creado, ebCordoba, LocalDateTime.of(2026, 3, 2, 9, 0));
        b119.cambiarEstado(cerrado, ebCordoba, LocalDateTime.of(2026, 3, 2, 10, 0));
        b119.cambiarEstado(enviado, ebCordoba, LocalDateTime.of(2026, 3, 3, 8, 0));
        b119.cambiarEstado(recibidoEnCMDestino, ebCordoba, LocalDateTime.of(2026, 3, 5, 11, 0));
        bolsinRepository.save(b119);

        // ---------- Bolsines enviados desde otras CM ----------
        bolsinEnviado(102, 50102, NAVTRACK, rosario, mendoza, ebRosario);
        bolsinEnviado(104, 50104, GEOPULSE, central, cordoba, ebCentral);
    }

    private ComisionMedica cm(String codigo, String nombre, String direccion) {
        String email = codigo.toLowerCase() + "@bolsines.example";
        return comisionMedicaRepository.save(new ComisionMedica(codigo, nombre, direccion, email, "0800-000-0000"));
    }

    // Crea un empleado; si se indica nombreUsuario también crea su usuario (contraseña: 1234)
    private Empleado empleado(String nombre, String apellido, String email, Rol rol,
                              ComisionMedica cm, String nombreUsuario) {
        Usuario usuario = null;
        if (nombreUsuario != null) {
            usuario = usuarioRepository.save(new Usuario(nombreUsuario, "1234"));
        }
        return empleadoRepository.save(new Empleado(nombre, apellido, email, rol, cm, usuario));
    }

    // Crea un bolsín con historial Creado -> Cerrado -> Enviado
    private void bolsinEnviado(int numero, int precinto, String modeloGPS,
                               ComisionMedica origen, ComisionMedica destino, Empleado responsable) {
        Bolsin bolsin = new Bolsin(numero, LocalDate.of(2026, 3, 6), precinto, 1200, modeloGPS, origen, destino);
        bolsin.cambiarEstado(creado, responsable, LocalDateTime.of(2026, 3, 6, 9, 0));
        bolsin.cambiarEstado(cerrado, responsable, LocalDateTime.of(2026, 3, 6, 17, 30));
        bolsin.cambiarEstado(enviado, responsable, LocalDateTime.of(2026, 3, 7, 8, 0));
        bolsinRepository.save(bolsin);
    }
}
