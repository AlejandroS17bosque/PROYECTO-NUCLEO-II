package co.edu.horarios.validacion;

import co.edu.horarios.dominio.Asignacion;

import java.util.List;
import java.util.Optional;

/**
 * Contexto del patron Strategy. Depende solo de la abstraccion ReglaValidacion (DIP);
 * agregar una regla no requiere modificarlo (OCP). Reporta todas las violaciones.
 */
public final class MotorValidacion {

    private final List<ReglaValidacion> reglas;

    public MotorValidacion(List<ReglaValidacion> reglas) {
        this.reglas = List.copyOf(reglas);
    }

    public ResultadoValidacion validar(Asignacion propuesta, ContextoValidacion contexto) {
        List<Violacion> violaciones = reglas.stream()
                .map(r -> r.evaluar(propuesta, contexto))
                .flatMap(Optional::stream)
                .toList();
        return new ResultadoValidacion(violaciones);
    }
}
