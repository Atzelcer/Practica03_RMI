public class RudeUtil {
    public static String rude(String nombres, String ap1, String ap2, String fechaDdMmYyyy) {
        String n = two(nombres);
        String a1 = two(ap1);
        String a2 = two(ap2);
        String f = fechaDdMmYyyy == null ? "" : fechaDdMmYyyy.replace("-", "");
        return n + a1 + a2 + f;
    }
    private static String two(String s) {
        if (s == null) return "XX";
        String t = s.trim();
        if (t.length() < 2) t = (t + "XX").substring(0, 2);
        return t.substring(0, 2);
    }
}
