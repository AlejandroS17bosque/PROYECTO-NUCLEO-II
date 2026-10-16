package co.edu.horarios.validacion.reglas;

import co.edu.horarios.config.GrillaHoraria;
import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-03 (y RES-08): la franja debe pertenecer a la grilla parametrizada. */
public final class Res03VentanaHoraria implements ReglaValidacion {

    private final GrillaHoraria grilla;

    public Res03VentanaHoraria(GrillaHoraria grilla) {
        this.grilla = grilla;
    }

    @Override public String codigo() { return "RES-03"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        if (grilla.contiene(propuesta.franja())) {
            return Optional.empty();
        }
        return Optional.of(violacion("La franja " + propuesta.franja()
                + " no pertenece a la oferta horaria permitida"));
    }
}
