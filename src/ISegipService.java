import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISegipService extends Remote {
    Respuesta verificar(String ci, String nombres, String apellido1, String apellido2) throws RemoteException;
}
