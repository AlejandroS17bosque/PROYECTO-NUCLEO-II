package co.edu.horarios.config;

import co.edu.horarios.dominio.Franja;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Genera las franjas validas a partir de la configuracion. Una franja fuera de la grilla no existe. */
public final class GrillaHoraria {

    private final List<Franja> franjas;
    private final Set<Franja> indice;

    public GrillaHoraria(ConfiguracionHorario config) {
        List<Franja> generadas = new ArrayList<>();
        for (DayOfWeek dia : DayOfWeek.values()) {
            VentanaDia ventana = config.ventanas().get(dia);
            if (ventana == null) {
                continue;
            }
            LocalTime cursor = ventana.apertura();
            while (!cursor.plus(config.duracionSesion()).isAfter(ventana.cierre())) {
                LocalTime fin = cursor.plus(config.duracionSesion());
                boolean cruzaAlmuerzo = cursor.isBefore(config.almuerzoFin()) && config.almuerzoInicio().isBefore(fin);
                if (cruzaAlmuerzo) {
                    cursor = config.almuerzoFin();
                } else {
                    generadas.add(new Franja(dia, cursor, fin));
                    cursor = fin;
                }
            }
        }
        this.franjas = List.copyOf(generadas);
        this.indice = Set.copyOf(generadas);
    }

    public List<Franja> franjas() {
        return franjas;
    }

    public boolean contiene(Franja franja) {
        return indice.contains(franja);
    }
}
