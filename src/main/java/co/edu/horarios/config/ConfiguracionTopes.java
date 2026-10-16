package co.edu.horarios.config;

import co.edu.horarios.dominio.Vinculacion;

import java.util.EnumMap;
import java.util.Map;

/** Tope de horas semanales por vinculacion (SUP-03). Valores por defecto son de ejemplo. */
public record ConfiguracionTopes(Map<Vinculacion, Integer> horasSemanales) {

    public ConfiguracionTopes {
        horasSemanales = Map.copyOf(horasSemanales);
    }

    public static ConfiguracionTopes porDefecto() {
        Map<Vinculacion, Integer> m = new EnumMap<>(Vinculacion.class);
        m.put(Vinculacion.TC, 32);
        m.put(Vinculacion.TRES_CUARTOS, 24);
        m.put(Vinculacion.MEDIO_TIEMPO, 16);
        m.put(Vinculacion.CUARTO_TIEMPO, 8);
        return new ConfiguracionTopes(m);
    }

    public int topeHoras(Vinculacion v) {
        Integer tope = horasSemanales.get(v);
        if (tope == null) {
            throw new IllegalStateException("No hay tope configurado para " + v);
        }
        return tope;
    }
}
