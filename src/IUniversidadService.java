import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUniversidadService extends Remote {
    Diploma emitirDiploma(String ci, String nombres, String apellido1, String apellido2, String fechaNacimiento, String carrera) throws RemoteException;
}
