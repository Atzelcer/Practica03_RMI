import java.rmi.Naming;

public class UniversidadClient {
    public static void main(String[] args) throws Exception {
        String uniHost = "25.44.192.169";
        int uniPort = 1099;
        String url = "rmi://" + uniHost + ":" + uniPort + "/UniversidadService";
        IUniversidadService uni = (IUniversidadService) Naming.lookup(url);

        String ci = "1140506";
        String nombres = "Walter Jhamil";
        String ap1 = "Segovia";
        String ap2 = "Arellano";
        String fecha = "11-02-1996";
        String carrera = "Ing. en Ciencias de la Computacion";
        String rude = RudeUtil.rude(nombres, ap1, ap2, fecha);

        System.out.println("CI: " + ci);
        System.out.println("Nombres: " + nombres);
        System.out.println("1erApellido: " + ap1);
        System.out.println("2doApellido: " + ap2);
        System.out.println("fecha_nacimiento: " + fecha);
        System.out.println("Carrera: " + carrera);
        System.out.println("RUDE: " + rude);

        Diploma d = uni.emitirDiploma(ci, nombres, ap1, ap2, fecha, carrera);

        if (d.getMensaje() == null || d.getMensaje().isEmpty()) {
            System.out.println("Diploma emitido:");
            System.out.println("Nombre: " + d.getNombreCompleto());
            System.out.println("Carrera: " + d.getCarrera());
            System.out.println("Fecha: " + d.getFecha());
        } else {
            System.out.println("Error: " + d.getMensaje());
        }
    }
}
