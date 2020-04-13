import chiffrement.Aes;
import chiffrement.RSA_raw;
import diversification.Diversification;
import utils.Utils;

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
import java.util.concurrent.ThreadLocalRandom;

public class POC2 {

    public static byte[] getBytesOfFile(String path){
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateRandomAES_key16(){
        String result = "";

        for (int i = 0 ; i < 16 ; ++i) {
            byte b =(byte) ThreadLocalRandom.current().nextInt(256);
            result += String.format("%02X", b);
        }
        return result;
    }

    public static void crypt() {
        /*Génération clefs AES*/
        SecureRandom alea = new SecureRandom();

        //TODO: On peut utiliser ça directement pour générer aléatoirement une clé AES
        byte[] clef = new byte[16];
        alea.nextBytes(clef);


        byte[] initialisation_vector = new byte[16];
        alea.nextBytes(initialisation_vector);

        String n = "94f28651e58a75781cfe69900174b86f855f092f09e3da2ad86b4ed964a84917e5ec60f4ee6e3adaa13962884e5cf8dae2e0d29c6168042ec9024ea11176a4ef031ac0f414918b7d13513ca1110ed80bd2532f8a7aab0314bf54fcaf621eda74263faf2a5921ffc515097a3c556bf86f2048a3c159fccfee6d916d38f7f23f21";
        String e = "44bb1ff6c2b674798e09075609b7883497ae2e2d7b06861ef9850e26d1456280523319021062c8743544877923fe65f85111792a98e4b887de8ffd13aef18ff7f6f736c821cfdad98af051e7caaa575d30b54ed9a6ee901bb0ffc17e25d444f8bfc5922325ee2ef94bd4ee15bede2ea12eb623ad507d6b246a1f0c3cc419f155";

        BigInteger public_module = new BigInteger(n,16);
        BigInteger public_exposant = new BigInteger(e, 16);

        try {
            //:TODO remodifier pour donner la clé à coder

            String clef_aes_courte = generateRandomAES_key16();
            byte[] clef_aes_etendu = Diversification.generateClefLongue(clef_aes_courte);
            System.out.println("La clé étendu fait " );
            //TODO: On ne doit pas user du public exposant ???
            byte[] encryptedKey_cipherData = RSA_raw.encryptInRSA_message_WithFollowingModules(clef_aes_courte.getBytes(),public_module,public_exposant);

            System.out.println(encryptedKey_cipherData.length); /*Taille de 128 bits*/

            System.out.println("Voici la clé AES utilisé "+clef_aes_courte+" taille de string :" +clef_aes_courte.length());

            Aes aes = new Aes();
            //TODO:Rajouter clef AES dans algo
            byte[] encryptedFile_cipherData = aes.chiffrer_CBC(Utils.getBytesOfFile("butokuden.jpg"),16,clef_aes_etendu, initialisation_vector);


            FileOutputStream output = new FileOutputStream("output.jpg", true);
            try {
                output.write(encryptedKey_cipherData);
                output.write(initialisation_vector);
                output.write(encryptedFile_cipherData);
            } finally {
                output.close();
            }

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