package co.edu.horarios.validacion;

public record Violacion(String codigoRegla, Severidad severidad, String mensaje) {
    @Override
    public String toString() {
        return "[" + codigoRegla + "/" + severidad + "] " + mensaje;
    }
}
