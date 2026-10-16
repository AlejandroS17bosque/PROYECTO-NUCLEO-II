package co.edu.horarios.validacion;

import co.edu.horarios.dominio.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

final class Fixtures {
    private Fixtures() { }

    static Franja franja(DayOfWeek dia, int ini, int fin) {
        return new Franja(dia, LocalTime.of(ini, 0), LocalTime.of(fin, 0));
    }

    static Docente docente(String id, Vinculacion v) {
        return new Docente(id, "Docente " + id, v, Set.of("MAT101"), DisponibilidadDocente.sinRestricciones());
    }

    static Docente docente(String id, Vinculacion v, DisponibilidadDocente d) {
        return new Docente(id, "Docente " + id, v, Set.of("MAT101"), d);
    }

    static Grupo grupo(String id, String codigoAsignatura) {
        return new Grupo(id, new Asignatura(codigoAsignatura, "Asignatura " + codigoAsignatura));
    }
}
