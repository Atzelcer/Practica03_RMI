import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class SegipServer extends UnicastRemoteObject implements ISegipService {

    private static final String HOST = "25.45.52.66";
    private static final int PORT = 1100;

    protected SegipServer() throws RemoteException { super(); }

    @Override
    public Respuesta verificar(String ci, String nombres, String ap1, String ap2) throws RemoteException {
        boolean ok = "1140506".equals(ci) && "Walter Jhamil".equals(nombres) && "Segovia".equals(ap1) && "Arellano".equals(ap2);
        if (ok) return new Respuesta(true, "Los Datos son correctos");
        return new Respuesta(false, "Los Datos del CI no son correctos");
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.rmi.server.hostname", HOST);
        try { LocateRegistry.createRegistry(PORT); } catch (Exception ignored) {}
        SegipServer srv = new SegipServer();
        String url = "rmi://" + HOST + ":" + PORT + "/SegipService";
        Naming.rebind(url, srv);
        System.out.println("SEGIP RMI listo en " + url);
    }
}
