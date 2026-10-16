package co.edu.horarios.validacion.reglas;

import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-05: respeta las restricciones horarias declaradas por el docente. */
public final class Res05DisponibilidadDocente implements ReglaValidacion {

    @Override public String codigo() { return "RES-05"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        if (propuesta.docente().disponibilidad().permite(propuesta.franja())) {
            return Optional.empty();
        }
        return Optional.of(violacion("La franja " + propuesta.franja()
                + " contradice la disponibilidad declarada por el docente " + propuesta.docente().id()));
    }
}
