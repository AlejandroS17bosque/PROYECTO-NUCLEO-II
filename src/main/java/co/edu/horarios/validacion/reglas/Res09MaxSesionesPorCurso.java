package co.edu.horarios.validacion.reglas;

import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-09: un grupo no puede superar el maximo de sesiones semanales (parametrizable). */
public final class Res09MaxSesionesPorCurso implements ReglaValidacion {

    private final int maxSesiones;

    public Res09MaxSesionesPorCurso(int maxSesiones) {
        if (maxSesiones < 1) {
            throw new IllegalArgumentException("maxSesiones debe ser >= 1");
        }
        this.maxSesiones = maxSesiones;
    }

    @Override public String codigo() { return "RES-09"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        long actuales = contexto.asignacionesExistentes().stream()
                .filter(a -> a.grupo().id().equals(propuesta.grupo().id()))
                .count();
        if (actuales + 1 > maxSesiones) {
            return Optional.of(violacion("El grupo " + propuesta.grupo().id()
                    + " superaria el maximo de " + maxSesiones + " sesiones semanales"));
        }
        return Optional.empty();
    }
}
