package co.edu.horarios.config;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;

/** Parametros de RES-03 y RES-08. Cambiar estos valores recalcula la grilla. */
public record ConfiguracionHorario(Map<DayOfWeek, VentanaDia> ventanas,
                                   LocalTime almuerzoInicio,
                                   LocalTime almuerzoFin,
                                   Duration duracionSesion) {

    public ConfiguracionHorario {
        ventanas = Map.copyOf(ventanas);
    }

    /** Lunes a viernes 07-22, sabado 07-13, almuerzo 12-13, sesiones de 2 h. */
    public static ConfiguracionHorario porDefecto() {
        VentanaDia semana = new VentanaDia(LocalTime.of(7, 0), LocalTime.of(22, 0));
        VentanaDia sabado = new VentanaDia(LocalTime.of(7, 0), LocalTime.of(13, 0));
        return new ConfiguracionHorario(
                Map.of(DayOfWeek.MONDAY, semana, DayOfWeek.TUESDAY, semana,
                        DayOfWeek.WEDNESDAY, semana, DayOfWeek.THURSDAY, semana,
                        DayOfWeek.FRIDAY, semana, DayOfWeek.SATURDAY, sabado),
                LocalTime.NOON, LocalTime.of(13, 0), Duration.ofHours(2));
    }
}
