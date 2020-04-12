import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static byte[] getBytesOfFile(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int byte_bijonction_int(byte b){
        int v = b & 0xFF;
        char[] c = new char[2];
        c[0] = HEX_ARRAY[v >>> 4];
        c[1] = HEX_ARRAY[v & 0x0F];
        //System.out.println("CHARAC :"+c[0]+c[1]);

        return (halfByteToNumber(c[0])*16)+halfByteToNumber(c[1]);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static int countOctetOfFile(String filename){
        int nbOctetsLus = 0, nbOctetsLusTotal=0;
        try {
            File fichier = new File(filename);
            FileInputStream fis = new FileInputStream(fichier);

            byte[] buffer = new byte[1024];
            nbOctetsLus = fis.read(buffer);                   // Lecture du premier morceau
            while (nbOctetsLus != -1) {
                if (nbOctetsLus!=-1)
                    nbOctetsLusTotal+=nbOctetsLus;
                nbOctetsLus = fis.read(buffer);                   // Lecture du morceau suivant
            }
            fis.close();
        } catch (Exception e) { e.printStackTrace(); }
        System.out.println("J'ai lu "+nbOctetsLusTotal+" octets");
        return nbOctetsLusTotal;
    }

    static void writeBytesInFile(byte[] bytes, File file)
    {
        try {

            // Initialize a pointer
            // in file using OutputStream
            OutputStream
                    os
                    = new FileOutputStream(file);

            // Starts writing the bytes in it
            os.write(bytes);
            System.out.println("Successfully"
                    + " byte inserted");

            // Close the file
            os.close();
        }

        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static void copyColumn(byte[] src, byte[] dest){
        for(int i=0; i<4;i++){
            dest[i] = src[i];
        }
    }

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

    public static int halfByteToNumber(Character a){
        switch(a) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 10;
            case 'b':
                return 11;
            case 'c':
                return 12;
            case 'd':
                return 13;
            case 'e':
                return 14;
            case 'f':
                return 15;
        }
        return -1;
    }

}