package co.edu.horarios.validacion;

import java.util.List;

public record ResultadoValidacion(List<Violacion> violaciones) {

    public ResultadoValidacion {
        violaciones = List.copyOf(violaciones);
    }

    /** Valido si no hay violaciones duras. Las blandas solo son advertencias. */
    public boolean esValido() {
        return violaciones.stream().noneMatch(v -> v.severidad() == Severidad.DURA);
    }

    public List<Violacion> errores() {
        return violaciones.stream().filter(v -> v.severidad() == Severidad.DURA).toList();
    }

    public List<Violacion> advertencias() {
        return violaciones.stream().filter(v -> v.severidad() == Severidad.BLANDA).toList();
    }

    public String diagnostico() {
        if (violaciones.isEmpty()) {
            return "Asignacion valida";
        }
        return String.join(System.lineSeparator(), violaciones.stream().map(Violacion::toString).toList());
    }
}
