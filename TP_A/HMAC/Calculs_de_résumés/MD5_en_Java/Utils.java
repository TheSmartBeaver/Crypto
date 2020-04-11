import java.util.Scanner;

import static java.lang.System.exit;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     *
     * @param bytes
     * @return Un tableau de byte en String
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     *
     * @param s Une String qui représente des hexadécimaux
     * @return Un tableau de bytes contenant les hexadécimaux décrit par "s"
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     *
     * @param a
     * @param b
     * @return le XOR de a et b
     */
    public static byte XORTwoBytes(byte a, byte b){
        //System.out.println("XOR "+a+" et "+b);
        int one = a;
        int two = b;
        int xor = one ^ two;

        byte c = (byte)(0xff & xor);

        /*Test qui sert à vérifier si XOr est réversible*/
        if((one^c)==two)
            ;
            //System.out.println("xor réversible ! :)");
        else {
            //System.out.println("xor pas réversible !!! c:" + (one ^ c) + " différent de two:" + two);
            exit(1);
        }
        return c;
    }

    /**
     *
     * @param a
     * @param b
     * @return le XOR de 2 tableau de byte
     */
    public static byte[] XORTwoByteArrays(byte[] a, byte[] b){
        assert a.length == b.length;
        byte[] xor = new byte[a.length];
        for (int i=0; i<a.length; i++)
            xor[i] = XORTwoBytes( a[i],b[i]);
        return xor;
    }

    /**
     *
     * @param taille
     * @return une suite d'octets 363636... de taille s
     */
    public static String generate_ipad(int taille){
        String ipad_str = new String();
        for(int i=0; i<taille/2; i++){
            ipad_str = ipad_str + "36";
        }

        return ipad_str;
    }

    /**
     *
     * @param taille
     * @return une suite d'octets 5c5c5c... de taille s sous forme de String
     */
    public static String generate_opad(int taille){
        String ipad_str = new String();
        for(int i=0; i<taille/2; i++){
            ipad_str = ipad_str + "5c";
        }

        return ipad_str;
    }
}
