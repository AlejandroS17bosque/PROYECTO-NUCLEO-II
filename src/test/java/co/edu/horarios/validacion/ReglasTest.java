package co.edu.horarios.validacion;

import co.edu.horarios.config.*;
import co.edu.horarios.dominio.*;
import co.edu.horarios.validacion.reglas.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static co.edu.horarios.validacion.Fixtures.*;
import static org.junit.jupiter.api.Assertions.*;

class ReglasTest {

    private static final ContextoValidacion VACIO = new ContextoValidacion(List.of());
    private final GrillaHoraria grilla = new GrillaHoraria(ConfiguracionHorario.porDefecto());

    // ---------- RES-01 ----------
    @Test
    void res01DetectaCruceDelMismoDocente() {
        Docente d = docente("D1", Vinculacion.TC);
        Franja f = franja(DayOfWeek.MONDAY, 7, 9);
        var existente = new Asignacion(d, grupo("G1", "MAT101"), f);
        var propuesta = new Asignacion(d, grupo("G2", "MAT101"), f);

        assertTrue(new Res01CruceDocente().evaluar(propuesta, new ContextoValidacion(List.of(existente))).isPresent());
    }

    @Test
    void res01PermiteMismaFranjaConOtroDocente() {
        Franja f = franja(DayOfWeek.MONDAY, 7, 9);
        var existente = new Asignacion(docente("D1", Vinculacion.TC), grupo("G1", "MAT101"), f);
        var propuesta = new Asignacion(docente("D2", Vinculacion.TC), grupo("G2", "MAT101"), f);

        assertTrue(new Res01CruceDocente().evaluar(propuesta, new ContextoValidacion(List.of(existente))).isEmpty());
    }

    @Test
    void res01PermiteFranjasContiguas() {
        Docente d = docente("D1", Vinculacion.TC);
        var existente = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.MONDAY, 7, 9));
        var propuesta = new Asignacion(d, grupo("G2", "MAT101"), franja(DayOfWeek.MONDAY, 9, 11));

        assertTrue(new Res01CruceDocente().evaluar(propuesta, new ContextoValidacion(List.of(existente))).isEmpty());
    }

    // ---------- RES-02 ----------
    @Test
    void res02BloqueaAlSuperarElTope() {
        Docente d = docente("D1", Vinculacion.CUARTO_TIEMPO); // 8 h = 4 sesiones
        List<Asignacion> existentes = new ArrayList<>();
        for (Franja f : grilla.franjas().subList(0, 4)) {
            existentes.add(new Asignacion(d, grupo("G" + f.hashCode(), "MAT101"), f));
        }
        var propuesta = new Asignacion(d, grupo("GX", "MAT101"), grilla.franjas().get(10));

        var regla = new Res02TopeHoras(ConfiguracionTopes.porDefecto());
        assertTrue(regla.evaluar(propuesta, new ContextoValidacion(existentes)).isPresent());
    }

    @Test
    void res02PermiteExactamenteElTope() {
        Docente d = docente("D1", Vinculacion.CUARTO_TIEMPO);
        List<Asignacion> existentes = new ArrayList<>();
        for (Franja f : grilla.franjas().subList(0, 3)) {
            existentes.add(new Asignacion(d, grupo("G" + f.hashCode(), "MAT101"), f));
        }
        var propuesta = new Asignacion(d, grupo("GX", "MAT101"), grilla.franjas().get(10));

        var regla = new Res02TopeHoras(ConfiguracionTopes.porDefecto());
        assertTrue(regla.evaluar(propuesta, new ContextoValidacion(existentes)).isEmpty());
    }

    @Test
    void res02UsaElTopeSegunVinculacion() {
        var topes = ConfiguracionTopes.porDefecto();
        assertEquals(32, topes.topeHoras(Vinculacion.TC));
        assertEquals(24, topes.topeHoras(Vinculacion.TRES_CUARTOS));
        assertEquals(16, topes.topeHoras(Vinculacion.MEDIO_TIEMPO));
        assertEquals(8, topes.topeHoras(Vinculacion.CUARTO_TIEMPO));
    }

    // ---------- RES-03 ----------
    @Test
    void res03RechazaFranjaFueraDeLaGrilla() {
        var regla = new Res03VentanaHoraria(grilla);
        Docente d = docente("D1", Vinculacion.TC);

        var domingo = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.SUNDAY, 7, 9));
        var almuerzo = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.MONDAY, 11, 13));
        var valida = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.MONDAY, 13, 15));

        assertTrue(regla.evaluar(domingo, VACIO).isPresent());
        assertTrue(regla.evaluar(almuerzo, VACIO).isPresent());
        assertTrue(regla.evaluar(valida, VACIO).isEmpty());
    }

    // ---------- RES-04 ----------
    @Test
    void res04ExigeIdoneidad() {
        Docente d = docente("D1", Vinculacion.TC); // habilitado solo para MAT101
        Franja f = franja(DayOfWeek.MONDAY, 7, 9);

        assertTrue(new Res04Idoneidad().evaluar(new Asignacion(d, grupo("G1", "FIS201"), f), VACIO).isPresent());
        assertTrue(new Res04Idoneidad().evaluar(new Asignacion(d, grupo("G1", "MAT101"), f), VACIO).isEmpty());
    }

    // ---------- RES-05 ----------
    @Test
    void res05RespetaHoraMinimaDeInicio() {
        var disp = new DisponibilidadDocente(Set.of(), LocalTime.of(16, 0));
        Docente d = docente("D1", Vinculacion.TC, disp);
        var regla = new Res05DisponibilidadDocente();

        var manana = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.MONDAY, 7, 9));
        var noche = new Asignacion(d, grupo("G1", "MAT101"), franja(DayOfWeek.MONDAY, 17, 19));

        assertTrue(regla.evaluar(manana, VACIO).isPresent());
        assertTrue(regla.evaluar(noche, VACIO).isEmpty());
    }

    @Test
    void res05RespetaFranjasBloqueadas() {
        Franja bloqueada = franja(DayOfWeek.TUESDAY, 9, 11);
        Docente d = docente("D1", Vinculacion.TC, new DisponibilidadDocente(Set.of(bloqueada), null));

        assertTrue(new Res05DisponibilidadDocente()
                .evaluar(new Asignacion(d, grupo("G1", "MAT101"), bloqueada), VACIO).isPresent());
    }

    // ---------- RES-09 ----------
    @Test
    void res09LimitaSesionesPorGrupo() {
        Grupo g = grupo("G1", "MAT101");
        List<Asignacion> existentes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            existentes.add(new Asignacion(docente("D" + i, Vinculacion.TC), g, grilla.franjas().get(i)));
        }
        var propuesta = new Asignacion(docente("D9", Vinculacion.TC), g, grilla.franjas().get(20));

        assertTrue(new Res09MaxSesionesPorCurso(4)
                .evaluar(propuesta, new ContextoValidacion(existentes)).isPresent());
        assertTrue(new Res09MaxSesionesPorCurso(5)
                .evaluar(propuesta, new ContextoValidacion(existentes)).isEmpty());
    }

    @Test
    void res09RechazaParametroInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Res09MaxSesionesPorCurso(0));
    }
}
