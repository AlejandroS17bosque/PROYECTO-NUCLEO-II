package co.edu.horarios.validacion;

import co.edu.horarios.config.*;
import co.edu.horarios.validacion.reglas.*;

import java.util.List;

/** Composicion del motor con las reglas del MVP. Unico lugar que conoce las reglas concretas. */
public final class MotorFactory {

    private MotorFactory() { }

    public static MotorValidacion crearMvp(GrillaHoraria grilla, ConfiguracionTopes topes, int maxSesionesPorCurso) {
        return new MotorValidacion(List.of(
                new Res01CruceDocente(),
                new Res02TopeHoras(topes),
                new Res03VentanaHoraria(grilla),
                new Res04Idoneidad(),
                new Res05DisponibilidadDocente(),
                new Res09MaxSesionesPorCurso(maxSesionesPorCurso)));
    }

    public static MotorValidacion crearPorDefecto() {
        return crearMvp(new GrillaHoraria(ConfiguracionHorario.porDefecto()), ConfiguracionTopes.porDefecto(), 4);
    }
}
