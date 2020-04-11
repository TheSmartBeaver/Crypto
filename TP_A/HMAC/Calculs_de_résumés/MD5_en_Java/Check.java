// -*- coding: utf-8 -*-

import java.io.*;
import java.security.*;

import static java.lang.System.exit;

public class Check
{

    public static void main(String[] args){
        checkEmailAuthencity(args[0]);
    }

    /**Vérifie authenticité du mail
     *
     * @param inputFile nom du fichier du mail à vérifier
     */
    public static void checkEmailAuthencity(String inputFile)
    {
        try {
            String secret, appendice, x_auth;
            x_auth = new String();
            secret = "c5dcb78732e1f3966647655229729843"; //Alain Turain !
            File fichier = new File(inputFile);

            BufferedReader br = new BufferedReader(new FileReader(fichier));

            String line;
            line = br.readLine();
            /*On va lire le fichier de sortie en entier ligne par ligne*/
            while(line!=null) {
                int pos = line.indexOf("X-AUTH: ");
                /*On trouve le champs X-AUTH*/
                if(pos>-1){
                    System.out.println("HASH X-AUTH "+line.substring(8,8+32));
                    /*On stocke appendice du mail*/
                    x_auth = line.substring(8, 8+32);
                }

                /*On trouve fin entête*/
                if (line.length() == 0) {
                    MessageDigest fonctionDeHachage = MessageDigest.getInstance("MD5");

                    byte[] secretInByteArray = secret.getBytes();
                    byte[] ipad = Utils.generate_ipad(secret.length()).getBytes();
                    /*on fait S XOR ipad*/
                    fonctionDeHachage.update(Utils.XORTwoByteArrays(secretInByteArray, ipad), 0, secret.length());

                    line = br.readLine();
                    /*On va lire le corps ligne par ligne pour calculer résumé*/
                    while (line != null) {
                        System.out.println("oo " + line);

                        line = line + "\r" + "\n";
                        fonctionDeHachage.update(line.getBytes(), 0, line.length());
                        line = br.readLine();
                    }

                    /*On calcule 1ère étape résumé à partir de S XOR ipad||c*/
                    byte[] md5Part = fonctionDeHachage.digest();
                    System.out.println("1ère étape calcul résumé calculé "+Utils.bytesToHex(md5Part));

                    /*Etape finale calcul résumé*/
                    fonctionDeHachage = MessageDigest.getInstance("MD5");
                    /*On ajoute S XOR opad*/
                    byte[] opad = Utils.generate_opad(secret.length()).getBytes();
                    fonctionDeHachage.update(Utils.XORTwoByteArrays(secretInByteArray, opad), 0, secret.length());
                    /*S XOR opad || md5Part*/
                    fonctionDeHachage.update(md5Part,0,md5Part.length);

                    /*Calcul final résumé selon RFC 2104*/
                    byte[] md5_RFC_2104 = fonctionDeHachage.digest();

                    /*On a notre résumé*/
                    appendice = Utils.bytesToHex(md5_RFC_2104);
                    System.out.println("Appendice final RFC 2104 : "+appendice);


                    /*On vérifie ensuite si le mail est authentique*/
                    if(appendice.equals(x_auth)) {
                        System.out.println("\nMail Authentique !");
                        exit(0);
                    }
                    else{
                        System.out.println("\nMail PAS authentique !!!!!");
                        exit(1);
                    }
                }

                line = br.readLine();
            }

        } catch (Exception e) { e.printStackTrace(); }
    }
}


