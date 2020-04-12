import chiffrement.RSA_raw;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

public class POC2 {

    public static byte[] getBytesOfFile(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void crypt() {
        /*Génération clefs AES*/
        SecureRandom alea = new SecureRandom();

        byte[] clef = new byte[16];
        alea.nextBytes(clef);
        SecretKeySpec secretKey = new SecretKeySpec(clef, "AES");


        byte[] initialisation_vector = new byte[16];
        alea.nextBytes(initialisation_vector);

        String n = "94f28651e58a75781cfe69900174b86f855f092f09e3da2ad86b4ed964a84917e5ec60f4ee6e3adaa13962884e5cf8dae2e0d29c6168042ec9024ea11176a4ef031ac0f414918b7d13513ca1110ed80bd2532f8a7aab0314bf54fcaf621eda74263faf2a5921ffc515097a3c556bf86f2048a3c159fccfee6d916d38f7f23f21";
        String e = "44bb1ff6c2b674798e09075609b7883497ae2e2d7b06861ef9850e26d1456280523319021062c8743544877923fe65f85111792a98e4b887de8ffd13aef18ff7f6f736c821cfdad98af051e7caaa575d30b54ed9a6ee901bb0ffc17e25d444f8bfc5922325ee2ef94bd4ee15bede2ea12eb623ad507d6b246a1f0c3cc419f155";

        BigInteger public_module = new BigInteger(n,16);
        BigInteger public_exposant = new BigInteger(e, 16);

        byte[] buffer = secretKey.getEncoded();

        try {
            KeyFactory RSAKeyUsine = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec specifPublic = new RSAPublicKeySpec(public_module, public_exposant);
            //PublicKey public_key = RSAKeyUsine.generatePublic(specifPublic);

            //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //cipher.init(Cipher.ENCRYPT_MODE, public_key);
            //byte[] encryptedKey_cipherData = cipher.doFinal(secretKey.getEncoded());
            //:TODO remodifier pour donner la clé à coder
            byte[] encryptedKey_cipherData = RSA_raw.encryptInRSA();

            System.out.println(encryptedKey_cipherData.length); /*Taille de 128 bits*/

            //cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //SecretKeySpec encryptedKey = new SecretKeySpec(encryptedKey_cipherData,"AES");
            //cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(initialisation_vector));
            //byte[] encryptedFile_cipherData = cipher.doFinal(getBytesOfFile("POC1/butokuden.jpg"));
            byte[] encryptedFile_cipherData = new byte[1];


            FileOutputStream output = new FileOutputStream("output.jpg", true);
            try {
                output.write(encryptedKey_cipherData);
                output.write(initialisation_vector);
                output.write(encryptedFile_cipherData);
            } finally {
                output.close();
            }

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String args[]){
        crypt();
    }
}