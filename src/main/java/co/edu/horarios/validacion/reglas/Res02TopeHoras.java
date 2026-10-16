package co.edu.horarios.validacion.reglas;

import co.edu.horarios.config.ConfiguracionTopes;
import co.edu.horarios.dominio.Asignacion;
import co.edu.horarios.validacion.*;

import java.util.Optional;

/** RES-02: la carga asignada no puede superar el tope de la vinculacion. */
public final class Res02TopeHoras implements ReglaValidacion {

    private final ConfiguracionTopes topes;

    public Res02TopeHoras(ConfiguracionTopes topes) {
        this.topes = topes;
    }

    @Override public String codigo() { return "RES-02"; }
    @Override public Severidad severidad() { return Severidad.DURA; }

    @Override
    public Optional<Violacion> evaluar(Asignacion propuesta, ContextoValidacion contexto) {
        long minutosActuales = contexto.asignacionesExistentes().stream()
                .filter(a -> a.docente().id().equals(propuesta.docente().id()))
                .mapToLong(a -> a.franja().duracion().toMinutes())
                .sum();
        long minutosResultantes = minutosActuales + propuesta.franja().duracion().toMinutes();
        int topeHoras = topes.topeHoras(propuesta.docente().vinculacion());

        if (minutosResultantes > topeHoras * 60L) {
            return Optional.of(violacion("Carga resultante de " + minutosResultantes / 60.0
                    + " h supera el tope de " + topeHoras + " h para vinculacion "
                    + propuesta.docente().vinculacion()));
        }
        return Optional.empty();
    }
}
