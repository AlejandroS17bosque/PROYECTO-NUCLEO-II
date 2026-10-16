package co.edu.horarios.validacion;

import co.edu.horarios.dominio.Asignacion;

import java.util.List;

/** Estado actual del horario contra el cual se valida una propuesta. */
public record ContextoValidacion(List<Asignacion> asignacionesExistentes) {
    public ContextoValidacion {
        asignacionesExistentes = List.copyOf(asignacionesExistentes);
    }
}
