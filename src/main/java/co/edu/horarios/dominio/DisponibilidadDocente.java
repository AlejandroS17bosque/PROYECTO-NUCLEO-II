package co.edu.horarios.dominio;

import java.time.LocalTime;
import java.util.Set;

/**
 * Restricciones horarias declaradas por el docente (RES-05).
 *
 * @param bloqueadas franjas en las que no puede dictar clase
 * @param iniciaDesde hora minima de inicio de clases (null si no aplica)
 */
public record DisponibilidadDocente(Set<Franja> bloqueadas, LocalTime iniciaDesde) {

    public DisponibilidadDocente {
        bloqueadas = Set.copyOf(bloqueadas);
    }

    public static DisponibilidadDocente sinRestricciones() {
        return new DisponibilidadDocente(Set.of(), null);
    }

    public boolean permite(Franja franja) {
        boolean bloqueada = bloqueadas.stream().anyMatch(franja::seSolapaCon);
        boolean antesDeLaHoraMinima = iniciaDesde != null && franja.inicio().isBefore(iniciaDesde);
        return !bloqueada && !antesDeLaHoraMinima;
    }
}
