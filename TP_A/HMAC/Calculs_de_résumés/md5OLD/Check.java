// -*- coding: utf-8 -*-

import java.io.*;
import java.security.*;
import java.util.StringTokenizer;

import static com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer.UTF_8;
import static java.lang.System.exit;

public class Check
{
    public static void main(String[] args)
    {
        try {
            String secret, appendice, x_auth;
            appendice = new String();
            x_auth = new String();
            secret = "c5dcb78732e1f3966647655229729843"; //Alain Turain !
            File fichier = new File("sortie.txt");

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
                    line = br.readLine();
                    /*On va lire le corps ligne par ligne pour calculer appendice*/
                    while (line != null) {
                        System.out.println("oo " + line);

                        line = line + "\r" + "\n";
                        fonctionDeHachage.update(line.getBytes(), 0, line.length());
                        line = br.readLine();
                    }

                    /*On rajoute le secret artificiellement au mail pour calcul appendice*/
                    fonctionDeHachage.update(secret.getBytes(), 0, secret.length());
                    byte[] resumeMD5 = fonctionDeHachage.digest();

                    /*On calcule appendice à partir de c||S*/
                    appendice = Utils.bytesToHex(resumeMD5);
                    System.out.println("HASH calculé : "+appendice);

                    /*On vérifie ensuite si le mail est authentique*/
                    if(appendice.equals(x_auth)) {
                        System.out.println("Mail Authentique !");
                        //exit(0);
                    }
                    else{
                        System.out.println("Mail PAS authentique !!!!!");
                        //exit(1);
                    }
                }

                line = br.readLine();
            }

            byte[] secretInBytes = Utils.hexStringToByteArray("c5dcb78732e1f3966647655229729843");
            System.out.println("Tentative déconversion secret " + Utils.bytesToHex(secretInBytes));

            //TODO:marche pas faire à la main 363636... et 5c5c5c... pour les convertir ensuite en byte'
            char[] ah = {0xc5,0xdc,0xb7,0x87,0x32,0xe1,0xf3,0x96,0x66,0x47,0x65,0x52,0x29,0x72,0x98,0x43};
            byte[] oh = new String(ah).getBytes();
            System.out.println("Tentative déconversion secret " + Utils.bytesToHex(oh));


        } catch (Exception e) { e.printStackTrace(); }
    }
}

/* 
   $
   $ cat butokuden.jpg | md5
   aeef572459c1bec5f94b8d62d5d134b5
   $ javac Resume.java
   $ java Resume
   Le résumé MD5 du fichier "butokuden.jpg" vaut: 0xaeef572459c1bec5f94b8d62d5d134b5
   $
*/

