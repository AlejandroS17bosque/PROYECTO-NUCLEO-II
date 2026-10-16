package co.edu.horarios.dominio;

import java.util.Objects;
import java.util.Set;

public record Docente(String id,
                      String nombre,
                      Vinculacion vinculacion,
                      Set<String> asignaturasHabilitadas,
                      DisponibilidadDocente disponibilidad) {

    public Docente {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(vinculacion, "vinculacion");
        Objects.requireNonNull(disponibilidad, "disponibilidad");
        asignaturasHabilitadas = Set.copyOf(asignaturasHabilitadas);
    }
}
