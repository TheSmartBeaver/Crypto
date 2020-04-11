// -*- coding: utf-8 -*-

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Random;

public class RSA_raw {
    private static BigInteger code, codeChiffré, codeDéchiffré ;
    private static BigInteger n ;      // Le module de la clef publique
    private static BigInteger e ;      // L'exposant de la clef publique
    private static BigInteger d ;      // L'exposant de la clef privée

    static void fabrique() {           // Fabrique d'une paire de clefs RSA (A MODIFIER)
        /*n = new BigInteger("196520034100071057065009920573", 10);
        e = new BigInteger("7", 10);
        d = new BigInteger("56148581171448620129544540223", 10);*/

        int taille_clef = 1024;

        Random alea = new SecureRandom(); /*Secure Random à la place de Random*/

        BigInteger p = new BigInteger(taille_clef, alea);
        while(!p.isProbablePrime(1)){
            p = new BigInteger(taille_clef, alea);
        }
        BigInteger q = new BigInteger(taille_clef, alea);
        while(!q.isProbablePrime(1)){
            q = new BigInteger(taille_clef, alea);
        }
        n = p.multiply(q);
        p.subtract(new BigInteger("1"));
        q.subtract(new BigInteger("1"));
        BigInteger w = p.multiply(q);
        System.out.println ("Valeur de n : " + n + "\net w : "+ q);

        /*-------------------*/

        //TODO: TROUVER d tq PGCD(d et w) = 1 ET tq 1<d<w-1 cad qu'il faut que d et w soit premier entre eux
        BigInteger upperLimit = w.subtract(BigInteger.valueOf(1));
        BigInteger randomNumber;
        do {
            randomNumber = new BigInteger(upperLimit.bitLength(), alea);
        } while (randomNumber.compareTo(upperLimit) >= 0);
        BigInteger gcd = randomNumber.gcd(w);

        while(!gcd.equals(BigInteger.valueOf(1))){
            do {
                randomNumber = new BigInteger(upperLimit.bitLength(), alea);
            } while (randomNumber.compareTo(upperLimit) >= 0);
            gcd = randomNumber.gcd(w);
        }
        d = randomNumber;
        //TODO: TROUVER e tq d*e = 1 (mod w)
        BigInteger paire[] = new BigInteger[2];
        gcd = euclideEtendu(d,w,paire); /*Toujours égal à 1*/
        System.out.println("solution modulo correcte =1 ? "+ (d.multiply(paire[0])).add(w.multiply(paire[1])));
        System.out.println("Nos paires : " + paire[0] + "\n" + paire[1]);
        e = paire[1];
        /*On récupère ensuite le reste de la division de e par w comme dit dans le cours pour avoir e < w */
        e = e.mod(w);

        //TODO: RESPECTER RFC 4871 ?
    }

    public static BigInteger euclideEtendu(BigInteger a, BigInteger b, BigInteger[] x)
    {
        // Base Case
        if (a.equals(BigInteger.valueOf(0)))
        {
            x[0] = BigInteger.valueOf(0);
            x[1] = BigInteger.valueOf(1);
            return b;
        }

        BigInteger[] t = {BigInteger.valueOf(1),BigInteger.valueOf(1)}; // To store results of recursive call
        BigInteger gcd = euclideEtendu(b.mod(a), a,t);

        // Update t using results of recursive
        // call

        x[0] = t[1].subtract((b.divide(a)).multiply(t[0]));
        x[1] = t[0];

        return gcd;
    }

    public static byte[] readFile(String path) throws IOException {
        byte [] encoded = Files.readAllBytes(Paths.get(path));
        return encoded;
    }

    public static BigInteger os2ip_Message(byte[] message) throws IOException {
        BigInteger x = BigInteger.valueOf(0);
        for(int i=0; i<message.length; i++){
            BigInteger to_be_multiplied = new BigInteger("256").pow(message.length-(i+1));
            BigInteger to_be_added = BigInteger.valueOf(message[i]).multiply(to_be_multiplied);
            x = x.add(to_be_added);
            //System.out.println("x = "+x+" to_be_mult = "+to_be_multiplied + " to_be_added = "+ to_be_added);
        }
        return x;
    }

    public static String toHex(byte[] données) {
        StringBuffer sb = new StringBuffer();
        for(byte k: données) sb.append(String.format("0x%02X ", k));
        sb.append(" (" + données.length + " octets)");
        return sb.toString();
    }

    /*Bourrage PKCS1*/
    public static byte[] contruct_em_message(byte[] m){
        byte[] em = new byte[128];
        em[0] = 0x00;
        em[1] = 0x02;
        for (int i = 2; i<(128-(m.length-3)); i++){
            em[i] = 0x55; /*TODO:REMPLACER par suite d'octet aléatoire*/
            //System.out.println("i : "+i+" "+em[i]);
        }

        em[128-(m.length+1)] = 0x00;
        int j = -1;
        for (int i = 128-(m.length); i<128; i++){
            em[i] = m[++j];
            //System.out.println("i : "+i+" "+em[i]);
        }
        return em;
    }

    public static byte[] encryptInRSA() throws IOException {
        /*On fabrique les clés*/
        fabrique();

        /*Il s'agit du message qu'on veut crypter*/
        byte[] m = { 0x4B, 0x59, 0x4F, 0x54, 0x4F } ;
        System.out.println("Message clair      : " + toHex(m) );

        /*TODO: On produit em soit le message m avec bourrage PKCS1*/
        byte[] em = contruct_em_message(m);
        System.out.println("NOTRE MESSAGE em : "+toHex(em));

        /*On convertit le message façon os2ip*/
        code = os2ip_Message(em);

        /* Affichage des clefs utilisées */
        System.out.println("Clef publique (n) : " + n);
        System.out.println("Clef publique (e) : " + e);
        System.out.println("Clef privée (d)   : " + d);

        /* On effectue d'abord le chiffrement RSA du code clair avec la clef publique */
        codeChiffré = code.modPow(e, n);
        System.out.println("Code chiffré      : " + codeChiffré);

        byte[] crypted_in_RSA = codeChiffré.toByteArray();
        System.out.println("La taille du chiffré est de " + crypted_in_RSA.length);

        return crypted_in_RSA;
    }

    public static void main(String[] args) throws IOException {
        /*On fabrique les clés*/
        fabrique();

        /*Il s'agit du message qu'on veut crypter*/
        byte[] m = { 0x4B, 0x59, 0x4F, 0x54, 0x4F } ;
        System.out.println("Message clair      : " + toHex(m) );

        /*TODO: On produit em soit le message m avec bourrage PKCS1*/
        byte[] em = contruct_em_message(m);
        System.out.println("NOTRE MESSAGE em : "+toHex(em));

        /*On convertit le message façon os2ip*/
        code = os2ip_Message(em);

        /* Affichage des clefs utilisées */
        System.out.println("Clef publique (n) : " + n);
        System.out.println("Clef publique (e) : " + e);
        System.out.println("Clef privée (d)   : " + d);

        /* On effectue d'abord le chiffrement RSA du code clair avec la clef publique */
        codeChiffré = code.modPow(e, n);
        System.out.println("Code chiffré      : " + codeChiffré);

        byte[] crypted_in_RSA = codeChiffré.toByteArray();
        System.out.println("La taille du chiffré est de " + crypted_in_RSA.length);
    }
}
