package co.edu.horarios.validacion.reglas;

import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-04: el docente debe estar habilitado para la asignatura. */
public final class Res04Idoneidad implements ReglaValidacion {

    @Override public String codigo() { return "RES-04"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        String codigoAsignatura = propuesta.grupo().asignatura().codigo();
        if (propuesta.docente().asignaturasHabilitadas().contains(codigoAsignatura)) {
            return Optional.empty();
        }
        return Optional.of(violacion("El docente " + propuesta.docente().id()
                + " no cumple idoneidad para la asignatura " + codigoAsignatura));
    }
}
