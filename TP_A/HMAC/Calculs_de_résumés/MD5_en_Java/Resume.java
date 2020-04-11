// -*- coding: utf-8 -*-

import java.io.*;
import java.security.*;

public class Resume {

    public static void main(String[] args) {
        generateRFC2104Email(args[0], args[1]);
    }

    /** Génère un email avec un appendice calculé selon RFC 2104
     *
     * @param nameInput nom du fichier contenant mail à calculer
     * @param nameOutput nom du fichier de sortie
     */
    public static void generateRFC2104Email(String nameInput, String nameOutput) {
        try {
            String secret, appendice;

            secret = "c5dcb78732e1f3966647655229729843"; //Alain Turain !
            //File fichier = new File("email1.txt");
            //File ficSortie = new File("sortie.txt");
            File fichier = new File(nameInput);
            File ficSortie = new File(nameOutput);

            appendice = Resume.calculateAppendice(fichier, secret);
            Resume.insertAppendice(fichier,ficSortie,appendice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Insère dans un mail un champs "X-Auth" pour y mettre un appendice
     *
     * @param inputFile nom du fichier contenant le mail dont on a calculé l'appendice
     * @param outputFile nom du fichier de sortie qui sera le mail + un champs X-AUTH en plus
     * @param appendice l'appendice qu'on veut insérer
     */
    static public void insertAppendice(File inputFile, File outputFile, String appendice){
        try {
            /*sert écriture nouveau fichier email*/
            FileOutputStream sortie = new FileOutputStream(outputFile);
            /*On va re-parcourir le mail*/
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String line = "";
            line = br.readLine();
            while (line != null) {

                /*On trouve fin entête et on rajoute champs X-AUTH*/
                if (line.length() == 0) {
                    sortie.write("X-AUTH: ".getBytes());
                    sortie.write(appendice.getBytes());
                    sortie.write("\r\n".getBytes());

                    /*On finit de recopier corps du texte*/
                    while (line != null) {
                        System.out.println("aa " + line);
                        line = line + "\r" + "\n";
                        sortie.write(line.getBytes());
                        line = br.readLine();

                    }
                }
                /*Sert à parcourit entête mail et à la recopier dans sortie*/
                if (line != null) {
                    line = line + "\r\n";
                    sortie.write(line.getBytes());
                    line = br.readLine();
                }
            }
            sortie.close();
        } catch (Exception e){e.printStackTrace();}
    }

    /**
     *
     * @param inputFile nom du fichier contenant le mail dont on veut calculer l'appendice
     * @param secret le secret qu'on utilise
     * @return l'appendice calculé
     */
    public static String calculateAppendice(File inputFile, String secret) {

        String appendice = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            /*On va lire le fichier d'entrée en entier ligne par ligne*/
            String line;
            line = br.readLine();
            while (line != null) {

                /*Si on a finit de parcourir entête du mail*/
                if (line.length() == 0) {
                    MessageDigest fonctionDeHachage = MessageDigest.getInstance("MD5");


                    byte[] secretInByteArray = secret.getBytes();
                    byte[] ipad = Utils.generate_ipad(secret.length()).getBytes();
                    /*on fait S XOR ipad*/
                    fonctionDeHachage.update(Utils.XORTwoByteArrays(secretInByteArray, ipad), 0, secret.length());

                    /*On va lire le corps du mail en entier*/
                    line = br.readLine();
                    while (line != null) {
                        System.out.println("oo " + line);
                        line = line + "\r" + "\n";

                        /*On rajoute le corps du mail ligne par ligne à la fonction de hachage*/
                        fonctionDeHachage.update(line.getBytes(), 0, line.length());
                        line = br.readLine();
                    }


                    /*On calcule 1ère étape résumé à partir de S XOR ipad||c*/
                    byte[] md5Part = fonctionDeHachage.digest();
                    System.out.println("1ère étape calcul résumé calculé " + Utils.bytesToHex(md5Part));

                    /*Etape finale calcul résumé*/
                    fonctionDeHachage = MessageDigest.getInstance("MD5");
                    /*On ajoute S XOR opad*/
                    byte[] opad = Utils.generate_opad(secret.length()).getBytes();
                    fonctionDeHachage.update(Utils.XORTwoByteArrays(secretInByteArray, opad), 0, secret.length());
                    /*S XOR opad || md5Part*/
                    fonctionDeHachage.update(md5Part, 0, md5Part.length);

                    /*Calcul final résumé selon RFC 2104*/
                    byte[] md5_RFC_2104 = fonctionDeHachage.digest();

                    /*On a notre résumé à insérer*/
                    appendice = Utils.bytesToHex(md5_RFC_2104);
                    System.out.println("Appendice final RFC 2104 : " + appendice);

                }
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appendice;
    }
}
