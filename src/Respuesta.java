import java.io.Serializable;

public class Respuesta implements Serializable {
    private final boolean estado;
    private final String mensaje;

    public Respuesta(boolean estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public boolean isEstado() { return estado; }
    public String getMensaje() { return mensaje; }
}
