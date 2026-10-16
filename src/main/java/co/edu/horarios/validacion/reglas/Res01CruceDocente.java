package co.edu.horarios.validacion.reglas;

import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-01: un docente no puede tener dos clases en la misma franja y dia. */
public final class Res01CruceDocente implements ReglaValidacion {

    @Override public String codigo() { return "RES-01"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        return contexto.asignacionesExistentes().stream()
                .filter(a -> a.docente().id().equals(propuesta.docente().id()))
                .filter(a -> a.franja().seSolapaCon(propuesta.franja()))
                .findFirst()
                .map(a -> violacion("El docente " + propuesta.docente().id() + " ya dicta el grupo "
                        + a.grupo().id() + " en " + a.franja()));
    }
}
