import java.io.*;
import java.security.MessageDigest;

public class Utils {
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
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

    /*public static int countNbCharactersInFile(String fileName) throws IOException
    {
        File file = new File(fileName);
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);

        String line;

        int characterCount = 0;

        // Reading line by line from the
        // file until a null is returned
        while((line = reader.readLine()) != null)
        {
            if(!(line.equals("")))
            {

                characterCount += line.length();
            }
        }
        System.out.println("Total number of characters = " + characterCount);

        return characterCount;
    }*/

}
