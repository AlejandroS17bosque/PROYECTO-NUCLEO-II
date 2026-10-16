package co.edu.horarios.validacion;

import co.edu.horarios.dominio.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static co.edu.horarios.validacion.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class MotorValidacionTest {

    private final MotorValidacion motor = MotorFactory.crearPorDefecto();

    @Test
    void aceptaUnaAsignacionValida() {
        var propuesta = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "MAT101"),
                franja(DayOfWeek.MONDAY, 7, 9));

        ResultadoValidacion r = motor.validar(propuesta, new ContextoValidacion(List.of()));

        assertTrue(r.esValido());
        assertTrue(r.violaciones().isEmpty());
    }

    @Test
    void reportaTodasLasViolacionesDuras() {
        // Domingo (RES-03), asignatura no habilitada (RES-04)
        var propuesta = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "FIS201"),
                franja(DayOfWeek.SUNDAY, 7, 9));

        ResultadoValidacion r = motor.validar(propuesta, new ContextoValidacion(List.of()));

        assertFalse(r.esValido());
        List<String> codigos = r.errores().stream().map(Violacion::codigoRegla).toList();
        assertTrue(codigos.containsAll(List.of("RES-03", "RES-04")));
        assertEquals(2, codigos.size());
    }

    @Test
    void unaViolacionBlandaNoInvalidaLaAsignacion() {
        ReglaValidacion blanda = new ReglaValidacion() {
            @Override public String codigo() { return "RES-06"; }
            @Override public Severidad severidad() { return Severidad.BLANDA; }
            @Override public Optional<Violacion> evaluar(Asignacion p, ContextoValidacion c) {
                return Optional.of(violacion("Sesiones en dias consecutivos"));
            }
        };
        var motorConBlanda = new MotorValidacion(List.of(blanda));
        var propuesta = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "MAT101"),
                franja(DayOfWeek.MONDAY, 7, 9));

        ResultadoValidacion r = motorConBlanda.validar(propuesta, new ContextoValidacion(List.of()));

        assertTrue(r.esValido());
        assertEquals(1, r.advertencias().size());
    }

    @Test
    void permiteAgregarReglasSinModificarElMotor() {
        ReglaValidacion siempreFalla = new ReglaValidacion() {
            @Override public String codigo() { return "RES-XX"; }
            @Override public Severidad severidad() { return Severidad.DURA; }
            @Override public Optional<Violacion> evaluar(Asignacion p, ContextoValidacion c) {
                return Optional.of(violacion("regla nueva"));
            }
        };
        var propuesta = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "MAT101"),
                franja(DayOfWeek.MONDAY, 7, 9));

        assertFalse(new MotorValidacion(List.of(siempreFalla))
                .validar(propuesta, new ContextoValidacion(List.of())).esValido());
    }

    @Test
    void validaEnMenosDeUnSegundoConCargaAlta() {
        // 5.000 asignaciones existentes de otros docentes
        List<Asignacion> existentes = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            existentes.add(new Asignacion(docente("X" + i, Vinculacion.TC), grupo("GX" + i, "MAT101"),
                    franja(DayOfWeek.MONDAY, 7, 9)));
        }
        var contexto = new ContextoValidacion(existentes);
        var propuesta = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "MAT101"),
                franja(DayOfWeek.MONDAY, 7, 9));

        assertTimeout(Duration.ofSeconds(1), () -> motor.validar(propuesta, contexto));
    }
}
