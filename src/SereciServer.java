import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SereciServer {

    private static final String HOST = "25.45.52.66";
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(PORT, InetAddress.getByName(HOST));
        System.out.println("SERECI UDP escuchando en " + HOST + ":" + PORT);
        byte[] buf = new byte[1024];
        while (true) {
            DatagramPacket req = new DatagramPacket(buf, buf.length);
            ds.receive(req);
            String in = new String(req.getData(), 0, req.getLength(), "UTF-8");
            String esperado = "Ver-fecha:" + "Walter Jhamil" + "," + "Segovia Arellano" + "," + "11-02-1996";
            String out = esperado.equals(in) ? "si:verificacion correcta" : "no:error fecha nacimiento no correcta";
            byte[] resp = out.getBytes("UTF-8");
            DatagramPacket r = new DatagramPacket(resp, resp.length, req.getAddress(), req.getPort());
            ds.send(r);
        }
    }
}
