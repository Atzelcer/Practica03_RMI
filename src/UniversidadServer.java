import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class UniversidadServer extends UnicastRemoteObject implements IUniversidadService {

    private static final String UNI_HOST = "25.44.192.169";
    private static final int UNI_PORT = 1099;

    private static final String SEGIP_HOST = "25.45.52.66";
    private static final int SEGIP_PORT = 1100;

    private static final String SEDUCA_HOST = "25.39.244.49";
    private static final int SEDUCA_PORT = 7000;

    private static final String SERECI_HOST = "25.45.52.66";
    private static final int SERECI_PORT = 8000;

    private static final int TCP_TIMEOUT_MS = 4000;
    private static final int UDP_TIMEOUT_MS = 4000;

    protected UniversidadServer() throws RemoteException { super(); }

    @Override
    public Diploma emitirDiploma(String ci, String nombres, String ap1, String ap2, String fecha, String carrera) throws RemoteException {
        StringBuilder errores = new StringBuilder();

        try {
            String url = "rmi://" + SEGIP_HOST + ":" + SEGIP_PORT + "/SegipService";
            ISegipService segip = (ISegipService) Naming.lookup(url);
            Respuesta r = segip.verificar(ci, nombres, ap1, ap2);
            if (!r.isEstado()) {
                appendErr(errores, "Los Datos del CI no son correctos");
            }
        } catch (Exception e) {
            appendErr(errores, "SEGIP no disponible");
        }

        try (Socket sock = new Socket()) {
            sock.connect(new InetSocketAddress(SEDUCA_HOST, SEDUCA_PORT), TCP_TIMEOUT_MS);
            sock.setSoTimeout(TCP_TIMEOUT_MS);
            String rude = RudeUtil.rude(nombres, ap1, ap2, fecha);
            String req = "verificar-rude:" + rude + "\n";
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
            BufferedReader r = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
            w.write(req);
            w.flush();
            String resp = r.readLine();
            if (resp == null) {
                appendErr(errores, "SEDUCA sin respuesta");
            } else {
                String[] parts = resp.split(":", 2);
                String estado = parts.length > 0 ? parts[0] : "no";
                String msg = parts.length > 1 ? parts[1].trim() : "";
                if (!"si".equalsIgnoreCase(estado)) {
                    appendErr(errores, msg.isEmpty() ? "no se encontro el titulo de bachiller" : msg);
                }
            }
        } catch (Exception e) {
            appendErr(errores, "SEDUCA no disponible");
        }

        try (DatagramSocket ds = new DatagramSocket()) {
            ds.setSoTimeout(UDP_TIMEOUT_MS);
            String apellidos = (ap1 + " " + ap2).trim();
            String payload = "Ver-fecha:" + nombres + "," + apellidos + "," + fecha;
            byte[] out = payload.getBytes("UTF-8");
            DatagramPacket p = new DatagramPacket(out, out.length, InetAddress.getByName(SERECI_HOST), SERECI_PORT);
            ds.send(p);
            byte[] buf = new byte[512];
            DatagramPacket resp = new DatagramPacket(buf, buf.length);
            ds.receive(resp);
            String ans = new String(resp.getData(), 0, resp.getLength(), "UTF-8");
            String[] parts = ans.split(":", 2);
            String estado = parts.length > 0 ? parts[0] : "no";
            String msg = parts.length > 1 ? parts[1].trim() : "";
            if (!"si".equalsIgnoreCase(estado)) {
                appendErr(errores, msg.isEmpty() ? "error fecha nacimiento no correcta" : msg);
            }
        } catch (Exception e) {
            appendErr(errores, "SERECI no disponible");
        }

        if (errores.length() == 0) {
            String nombreCompleto = (nombres + " " + ap1 + " " + ap2).trim();
            return new Diploma(nombreCompleto, carrera, fecha, "");
        } else {
            return new Diploma("", "", "", errores.toString());
        }
    }

    private static void appendErr(StringBuilder sb, String msg) {
        if (sb.length() > 0) sb.append("; ");
        sb.append(msg);
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.rmi.server.hostname", UNI_HOST);
        try { LocateRegistry.createRegistry(UNI_PORT); } catch (Exception ignored) {}
        UniversidadServer srv = new UniversidadServer();
        String url = "rmi://" + UNI_HOST + ":" + UNI_PORT + "/UniversidadService";
        Naming.rebind(url, srv);
        System.out.println("Universidad RMI listo en " + url);
        System.out.println("Dependencias -> SEGIP " + SEGIP_HOST + ":" + SEGIP_PORT + " | SEDUCA " + SEDUCA_HOST + ":" + SEDUCA_PORT + " | SERECI " + SERECI_HOST + ":" + SERECI_PORT);
    }
}
