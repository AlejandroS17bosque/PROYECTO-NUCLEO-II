package co.edu.horarios.dominio;

/** Una sesion: un docente dicta un grupo en una franja. */
public record Asignacion(Docente docente, Grupo grupo, Franja franja) { }
