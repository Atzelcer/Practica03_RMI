import java.io.Serializable;

public class Diploma implements Serializable {
    private final String nombreCompleto;
    private final String carrera;
    private final String fecha;
    private final String mensaje;

    public Diploma(String nombreCompleto, String carrera, String fecha, String mensaje) {
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCarrera() { return carrera; }
    public String getFecha() { return fecha; }
    public String getMensaje() { return mensaje; }
}
