// -*- coding: utf-8 -*-

import java.io.*;
import java.security.*;
import java.util.StringTokenizer;

import static com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer.UTF_8;

public class Resume
{
    public static void main(String[] args)
    {
        try {
            String secret, appendice;
            appendice = new String();
            secret = "c5dcb78732e1f3966647655229729843"; //Alain Turain !
            File fichier = new File("email1.txt");
            File ficSortie = new File("sortie.txt");
            //TODO: Faire écriture 2ème fichier
            /*sert écriture nouveau fichier email*/
            FileOutputStream sortie = new FileOutputStream(ficSortie);
            BufferedReader br = new BufferedReader(new FileReader(fichier));

            /*On va lire le fichier d'entrée en entier ligne par ligne*/
            String line;
            line = br.readLine();
            while(line!=null) {

                /*Si on a finit de parcourir entête du mail*/
                if (line.length()==0) {
                    MessageDigest fonctionDeHachage = MessageDigest.getInstance("MD5");

                    /*On va lire le corps du mail en entier*/
                    line = br.readLine();
                    while (line!=null){
                        System.out.println("oo "+line);
                        line = line+"\r"+"\n";

                        /*On rajoute le corps du mail ligne par ligne à la fonction de hachage*/
                        fonctionDeHachage.update(line.getBytes(), 0, line.length());
                        line = br.readLine();
                    }

                    /*On rajoute le secret artificiellement au mail pour calcul appendice*/
                    fonctionDeHachage.update(secret.getBytes(), 0, secret.length());

                    /*On calcule appendice à partir de c||S*/
                    byte[] resumeMD5 = fonctionDeHachage.digest();

                    System.out.println("Appendice calculé "+Utils.bytesToHex(resumeMD5));
                    appendice = Utils.bytesToHex(resumeMD5);
                    continue; /*On sort de la boucle while principale*/
                }

                if(line!=null) {
                    line = br.readLine();
                }
            }

            /*On va re-parcourir le mail*/
            br = new BufferedReader(new FileReader(fichier));
            line = br.readLine();
            while(line!=null) {

                /*On trouve fin entête et on rajoute champs X-AUTH*/
                if (line.length()==0) {
                    sortie.write("X-AUTH: ".getBytes());
                    sortie.write(appendice.getBytes());
                    sortie.write("\r\n".getBytes());

                    /*On finit de recopier corps du texte*/
                    while (line!=null){
                        System.out.println("oo "+line);
                        line = line+"\r"+"\n";
                        sortie.write(line.getBytes());
                        line = br.readLine();
                        continue;
                    }
                }
                if(line!=null) {
                    line = line + "\r\n";
                    sortie.write(line.getBytes());
                    line = br.readLine();
                }
            }
            //sortie.write(secret.getBytes());
            sortie.close();
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

