package co.edu.horarios.validacion;

import co.edu.horarios.dominio.Asignacion;

import java.util.Optional;

/** Estrategia: contrato comun de toda regla institucional. */
public interface ReglaValidacion {

    String codigo();

    Severidad severidad();

    /** Devuelve la violacion si la propuesta incumple la regla, o vacio si la cumple. */
    Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto);

    default Violacion violacion(String mensaje) {
        return new Violacion(codigo(), severidad(), mensaje);
    }
}
