package co.edu.horarios.dominio;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/** Bloque de tiempo en un dia de la semana. Intervalo semiabierto [inicio, fin). */
public record Franja(DayOfWeek dia, LocalTime inicio, LocalTime fin) {

    public Franja {
        Objects.requireNonNull(dia, "dia");
        Objects.requireNonNull(inicio, "inicio");
        Objects.requireNonNull(fin, "fin");
        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la de fin");
        }
    }

    public Duration duracion() {
        return Duration.between(inicio, fin);
    }

    public boolean seSolapaCon(Franja otra) {
        return dia == otra.dia && inicio.isBefore(otra.fin) && otra.inicio.isBefore(fin);
    }

    @Override
    public String toString() {
        return dia + " " + inicio + "-" + fin;
    }
}
