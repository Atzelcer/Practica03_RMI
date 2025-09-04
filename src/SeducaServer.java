import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SeducaServer {

    private static final String HOST = "25.39.244.49"; // IP Hamachi
    private static final int PORT = 7000;

    private static final boolean FORCE_FAIL = Boolean.parseBoolean(System.getProperty("seduca.fail", "false"));
    private static final String RUDE_OK = System.getProperty("seduca.rude", "WaSeAr11021996");
    
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(HOST));
        System.out.println("SEDUCA TCP escuchando en " + HOST + ":" + PORT);

        while (true) {
            try (Socket s = server.accept()) {
                s.setSoTimeout(4000);
                BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF-8"));
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF-8"));

                String line = r.readLine();
                String resp;

                if (line == null) {
                    resp = "no:no se encontro el titulo de bachiller";
                } else if (FORCE_FAIL) {
                    resp = "no:no se encontro el titulo de bachiller";
                } else {
                    String pref = "verificar-rude:";
                    if (line.startsWith(pref)) {
                        String rude = line.substring(pref.length()).trim();
                        if (RUDE_OK.equals(rude)) {
                            resp = "si:verificado con exito";
                        } else {
                            resp = "no:no se encontro el titulo de bachiller";
                        }
                    } else {
                        resp = "no:no se encontro el titulo de bachiller";
                    }
                }
                w.write(resp);
                w.write("\n");
                w.flush();
            } catch (Exception e) {
                System.out.println("conexion rechazada");
            }
        }
    }
}