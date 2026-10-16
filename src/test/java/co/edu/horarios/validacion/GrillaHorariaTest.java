package co.edu.horarios.validacion;

import co.edu.horarios.config.*;
import co.edu.horarios.dominio.Franja;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static co.edu.horarios.validacion.Fixtures.franja;
import static org.junit.jupiter.api.Assertions.*;

class GrillaHorariaTest {

    private final GrillaHoraria grilla = new GrillaHoraria(ConfiguracionHorario.porDefecto());

    @Test
    void generaLas32FranjasSemanales() {
        assertEquals(32, grilla.franjas().size());
    }

    @Test
    void sabadoTieneDosBloques() {
        long sabado = grilla.franjas().stream().filter(f -> f.dia() == DayOfWeek.SATURDAY).count();
        assertEquals(2, sabado);
    }

    @Test
    void todasLasFranjasDuranDosHoras() {
        assertTrue(grilla.franjas().stream().allMatch(f -> f.duracion().toHours() == 2));
    }

    @Test
    void ningunaFranjaCruzaElAlmuerzo() {
        Franja almuerzo = new Franja(DayOfWeek.MONDAY, LocalTime.NOON, LocalTime.of(13, 0));
        assertTrue(grilla.franjas().stream().noneMatch(f -> f.seSolapaCon(almuerzo)
                || (f.dia() != DayOfWeek.MONDAY && f.inicio().isBefore(LocalTime.of(13, 0)) && f.fin().isAfter(LocalTime.NOON))));
    }

    @Test
    void noExistenFranjasEnDomingo() {
        assertTrue(grilla.franjas().stream().noneMatch(f -> f.dia() == DayOfWeek.SUNDAY));
        assertFalse(grilla.contiene(franja(DayOfWeek.SUNDAY, 7, 9)));
    }
}
