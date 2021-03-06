package chiffrement;

// -*- coding: utf-8 -*-

import diversification.Diversification;
import jdk.jshell.execution.Util;

import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

import utils.Utils;
import bourrage.PKCS5;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class Aes {

    /* La clef courte K utilisée aujourd'hui est formée de 16 octets nuls */
    int longueur_de_la_clef = 16 ;
    static byte K[] = {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    } ;


    /* Résultat du TP précédent : diversification de la clef courte K en une clef étendue W */

    static int Nr = 10;
    static int Nk = 4;
    int longueur_de_la_clef_etendue = 176;

    public byte W[] = {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63, (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63,
            (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63, (byte)0x62, (byte)0x63, (byte)0x63, (byte)0x63,
            (byte)0x9B, (byte)0x98, (byte)0x98, (byte)0xC9, (byte)0xF9, (byte)0xFB, (byte)0xFB, (byte)0xAA,
            (byte)0x9B, (byte)0x98, (byte)0x98, (byte)0xC9, (byte)0xF9, (byte)0xFB, (byte)0xFB, (byte)0xAA,
            (byte)0x90, (byte)0x97, (byte)0x34, (byte)0x50, (byte)0x69, (byte)0x6C, (byte)0xCF, (byte)0xFA,
            (byte)0xF2, (byte)0xF4, (byte)0x57, (byte)0x33, (byte)0x0B, (byte)0x0F, (byte)0xAC, (byte)0x99,
            (byte)0xEE, (byte)0x06, (byte)0xDA, (byte)0x7B, (byte)0x87, (byte)0x6A, (byte)0x15, (byte)0x81,
            (byte)0x75, (byte)0x9E, (byte)0x42, (byte)0xB2, (byte)0x7E, (byte)0x91, (byte)0xEE, (byte)0x2B,
            (byte)0x7F, (byte)0x2E, (byte)0x2B, (byte)0x88, (byte)0xF8, (byte)0x44, (byte)0x3E, (byte)0x09,
            (byte)0x8D, (byte)0xDA, (byte)0x7C, (byte)0xBB, (byte)0xF3, (byte)0x4B, (byte)0x92, (byte)0x90,
            (byte)0xEC, (byte)0x61, (byte)0x4B, (byte)0x85, (byte)0x14, (byte)0x25, (byte)0x75, (byte)0x8C,
            (byte)0x99, (byte)0xFF, (byte)0x09, (byte)0x37, (byte)0x6A, (byte)0xB4, (byte)0x9B, (byte)0xA7,
            (byte)0x21, (byte)0x75, (byte)0x17, (byte)0x87, (byte)0x35, (byte)0x50, (byte)0x62, (byte)0x0B,
            (byte)0xAC, (byte)0xAF, (byte)0x6B, (byte)0x3C, (byte)0xC6, (byte)0x1B, (byte)0xF0, (byte)0x9B,
            (byte)0x0E, (byte)0xF9, (byte)0x03, (byte)0x33, (byte)0x3B, (byte)0xA9, (byte)0x61, (byte)0x38,
            (byte)0x97, (byte)0x06, (byte)0x0A, (byte)0x04, (byte)0x51, (byte)0x1D, (byte)0xFA, (byte)0x9F,
            (byte)0xB1, (byte)0xD4, (byte)0xD8, (byte)0xE2, (byte)0x8A, (byte)0x7D, (byte)0xB9, (byte)0xDA,
            (byte)0x1D, (byte)0x7B, (byte)0xB3, (byte)0xDE, (byte)0x4C, (byte)0x66, (byte)0x49, (byte)0x41,
            (byte)0xB4, (byte)0xEF, (byte)0x5B, (byte)0xCB, (byte)0x3E, (byte)0x92, (byte)0xE2, (byte)0x11,
            (byte)0x23, (byte)0xE9, (byte)0x51, (byte)0xCF, (byte)0x6F, (byte)0x8F, (byte)0x18, (byte)0x8E
    };

    /* Le bloc à chiffrer aujourd'hui: 16 octets nuls */

	/*public byte State[] = {
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    };*/

    public byte State[] = {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
    } ;


    /* Programme principal */

    //TODO: tester validité avec : openssl enc -aes-128-cbc -K 0 -iv 0 -in butokuden.jpg > butokuden_c.jpg
    //TODO: md5sum butokuden_c.jpg

    public static String generateRandomAES_key16(){
        String result = "";

        for (int i = 0 ; i < 16 ; ++i) {
            byte b =(byte) ThreadLocalRandom.current().nextInt(256);
            result += String.format("%02X", b);
        }
        return result;
    }

    public static void main(String args[]) {
        Aes aes = new Aes();
        //System.out.println("LOL" +aes.SBox[0xC6]); //TODO: Enlever exit
        System.out.println(Utils.byte_bijonction_int((byte)0xff));
        System.out.println("taille SBOX :"+aes.SBox.length);
        //exit(1);
        System.out.println("Le bloc \"State\" en entrée vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;
        //aes.chiffrer();
        String clef_aes_courte = generateRandomAES_key16();
        aes.W = Diversification.generateClefLongue(clef_aes_courte);

        byte[] to_be_written_in_file = aes.chiffrer_CBC(PKCS5.getPkcs5OfFile("butokuden.jpg",16),16,aes.W,aes.State);
        System.out.println("Le bloc \"State\" en sortie vaut : ") ;
        aes.afficher_le_bloc(aes.State) ;

        File sortie = new File("sortie_PCKCS5_CBC.jpg");
        Utils.writeBytesInFile(to_be_written_in_file, sortie);
    }

    public void afficher_le_bloc(byte M[]) {
        for (int i=0; i<4; i++) { // Lignes 0 à 3
            System.out.print("          ");
            for (int j=0; j<4; j++) { // Colonnes 0 à 3
                System.out.print(String.format("%02X ", M[4*j+i]));
            }
            System.out.println();
        }
    }

    public void chiffrer(){
        AddRoundKey(0);
        for (int i = 1; i < Nr; i++) {
            //System.out.println("i de chiffrer :"+i);
            SubBytes();
            ShiftRows();
            MixColumns();
            AddRoundKey(i);
        }
        SubBytes();
        ShiftRows();
        AddRoundKey(Nr);
    }

    public void copyInArrayAtPos(byte[] array_src, byte[] array_dest, int pos, int taille_bloc){
        for (int i=0; i<taille_bloc; i++){
            array_dest[i+pos*taille_bloc] = array_src[i];
        }
    }

    public byte[] chiffrer_CBC(byte[] bytesOfFile, int taille_bloc, byte[] clef_etendu, byte[] IV){
        State = IV;

        W=clef_etendu; //TODO: Enlever si je veux continuer à corriger CBC

        int nb_bytes_fic = bytesOfFile.length;
        int nb_blocs = nb_bytes_fic/taille_bloc;
        byte[] result = new byte[nb_bytes_fic];

        for(int i=0; i<nb_blocs; i++){
            System.out.println("NUM blocs "+i+" nb blocs :"+nb_blocs);
            Utils.copyColumn(xorBlocs(get_n_bloc_of_BytesArray(bytesOfFile,i,16),State),State);
            chiffrer();
            copyInArrayAtPos(State,result,i,16);
        }
        return result;
    }

    public byte[] xorBlocs(byte[] a, byte[] b){
        byte[] result = new byte[a.length];
        for (int i=0; i<a.length; i++)
            result[i] = Utils.XORTwoBytes(a[i],b[i]);
        return result;
    }

    /* Table de substitution déjà utilisée lors du TP précédent */

    public byte[] SBox = {
            (byte)0x63, (byte)0x7C, (byte)0x77, (byte)0x7B, (byte)0xF2, (byte)0x6B, (byte)0x6F, (byte)0xC5,
            (byte)0x30, (byte)0x01, (byte)0x67, (byte)0x2B, (byte)0xFE, (byte)0xD7, (byte)0xAB, (byte)0x76,
            (byte)0xCA, (byte)0x82, (byte)0xC9, (byte)0x7D, (byte)0xFA, (byte)0x59, (byte)0x47, (byte)0xF0,
            (byte)0xAD, (byte)0xD4, (byte)0xA2, (byte)0xAF, (byte)0x9C, (byte)0xA4, (byte)0x72, (byte)0xC0,
            (byte)0xB7, (byte)0xFD, (byte)0x93, (byte)0x26, (byte)0x36, (byte)0x3F, (byte)0xF7, (byte)0xCC,
            (byte)0x34, (byte)0xA5, (byte)0xE5, (byte)0xF1, (byte)0x71, (byte)0xD8, (byte)0x31, (byte)0x15,
            (byte)0x04, (byte)0xC7, (byte)0x23, (byte)0xC3, (byte)0x18, (byte)0x96, (byte)0x05, (byte)0x9A,
            (byte)0x07, (byte)0x12, (byte)0x80, (byte)0xE2, (byte)0xEB, (byte)0x27, (byte)0xB2, (byte)0x75,
            (byte)0x09, (byte)0x83, (byte)0x2C, (byte)0x1A, (byte)0x1B, (byte)0x6E, (byte)0x5A, (byte)0xA0,
            (byte)0x52, (byte)0x3B, (byte)0xD6, (byte)0xB3, (byte)0x29, (byte)0xE3, (byte)0x2F, (byte)0x84,
            (byte)0x53, (byte)0xD1, (byte)0x00, (byte)0xED, (byte)0x20, (byte)0xFC, (byte)0xB1, (byte)0x5B,
            (byte)0x6A, (byte)0xCB, (byte)0xBE, (byte)0x39, (byte)0x4A, (byte)0x4C, (byte)0x58, (byte)0xCF,
            (byte)0xD0, (byte)0xEF, (byte)0xAA, (byte)0xFB, (byte)0x43, (byte)0x4D, (byte)0x33, (byte)0x85,
            (byte)0x45, (byte)0xF9, (byte)0x02, (byte)0x7F, (byte)0x50, (byte)0x3C, (byte)0x9F, (byte)0xA8,
            (byte)0x51, (byte)0xA3, (byte)0x40, (byte)0x8F, (byte)0x92, (byte)0x9D, (byte)0x38, (byte)0xF5,
            (byte)0xBC, (byte)0xB6, (byte)0xDA, (byte)0x21, (byte)0x10, (byte)0xFF, (byte)0xF3, (byte)0xD2,
            (byte)0xCD, (byte)0x0C, (byte)0x13, (byte)0xEC, (byte)0x5F, (byte)0x97, (byte)0x44, (byte)0x17,
            (byte)0xC4, (byte)0xA7, (byte)0x7E, (byte)0x3D, (byte)0x64, (byte)0x5D, (byte)0x19, (byte)0x73,
            (byte)0x60, (byte)0x81, (byte)0x4F, (byte)0xDC, (byte)0x22, (byte)0x2A, (byte)0x90, (byte)0x88,
            (byte)0x46, (byte)0xEE, (byte)0xB8, (byte)0x14, (byte)0xDE, (byte)0x5E, (byte)0x0B, (byte)0xDB,
            (byte)0xE0, (byte)0x32, (byte)0x3A, (byte)0x0A, (byte)0x49, (byte)0x06, (byte)0x24, (byte)0x5C,
            (byte)0xC2, (byte)0xD3, (byte)0xAC, (byte)0x62, (byte)0x91, (byte)0x95, (byte)0xE4, (byte)0x79,
            (byte)0xE7, (byte)0xC8, (byte)0x37, (byte)0x6D, (byte)0x8D, (byte)0xD5, (byte)0x4E, (byte)0xA9,
            (byte)0x6C, (byte)0x56, (byte)0xF4, (byte)0xEA, (byte)0x65, (byte)0x7A, (byte)0xAE, (byte)0x08,
            (byte)0xBA, (byte)0x78, (byte)0x25, (byte)0x2E, (byte)0x1C, (byte)0xA6, (byte)0xB4, (byte)0xC6,
            (byte)0xE8, (byte)0xDD, (byte)0x74, (byte)0x1F, (byte)0x4B, (byte)0xBD, (byte)0x8B, (byte)0x8A,
            (byte)0x70, (byte)0x3E, (byte)0xB5, (byte)0x66, (byte)0x48, (byte)0x03, (byte)0xF6, (byte)0x0E,
            (byte)0x61, (byte)0x35, (byte)0x57, (byte)0xB9, (byte)0x86, (byte)0xC1, (byte)0x1D, (byte)0x9E,
            (byte)0xE1, (byte)0xF8, (byte)0x98, (byte)0x11, (byte)0x69, (byte)0xD9, (byte)0x8E, (byte)0x94,
            (byte)0x9B, (byte)0x1E, (byte)0x87, (byte)0xE9, (byte)0xCE, (byte)0x55, (byte)0x28, (byte)0xDF,
            (byte)0x8C, (byte)0xA1, (byte)0x89, (byte)0x0D, (byte)0xBF, (byte)0xE6, (byte)0x42, (byte)0x68,
            (byte)0x41, (byte)0x99, (byte)0x2D, (byte)0x0F, (byte)0xB0, (byte)0x54, (byte)0xBB, (byte)0x16};



    /* Fonction mystérieuse qui calcule le produit de deux octets */

    byte gmul(byte a1, byte b1) {
        int a = Byte.toUnsignedInt(a1);
        int b = Byte.toUnsignedInt(b1);
        int p = 0;
        int hi_bit_set;
        for(int i = 0; i < 8; i++) {
            if((b & 1) == 1) p ^= a;
            hi_bit_set =  (a & 0x80);
            a <<= 1;
            if(hi_bit_set == 0x80) a ^= 0x1b;
            b >>= 1;
        }
        return (byte) (p & 0xFF);
    }

    /* Partie à compléter pour ce TP */

    public byte[] getRond(int r){
        byte[] a = new byte[16];
        for (int i=0; i<16; i++){
            a[i] = W[i+r*16];
        }
        return a;
    }

    public byte[] get_n_bloc_of_BytesArray(byte[] array, int pos, int taille_bloc){
        byte[] a = new byte[taille_bloc];
        for (int i=0; i<taille_bloc; i++){
            a[i] = array[i+pos*taille_bloc];
        }
        return a;
    }

    public void AddRoundKey(int r){
        byte a[] = getRond(r);
        for(int i=0; i<16; i++){
            State[i] = Utils.XORTwoBytes(a[i],State[i]);
        }
    }

    public void SubBytes(){//TODO: blem avec octet commençant par une lettre -> -58 IL FAUT POUVOIR ASSIGNER UN NOMBRE POUR UN OCTET. TROUVER une bijonction
        //System.out.println("State avant SubBytes");
        //afficher_le_bloc(State);
        int state_index;
        for(int i = 0; i<State.length; i++){
            state_index = Utils.byte_bijonction_int(State[i]);
            //System.out.println(i+" "+state_index); System.out.print(String.format("%02X ", State[i]));
            State[i] = SBox[state_index]; /*Bizarre comme fonction ?????*/
            //afficher_le_bloc(State);
        }
    }

    public void ShiftRows(){
        byte t[] = new byte[State.length];

        for(int i = 0; i <= State.length - 1; i++){
            t[i] = State[i];
        }

        t[1] = State[5];
        t[5] = State[9];
        t[9] = State[13];
        t[13] = State[1];

        t[2] = State[10];
        t[6] = State[14];
        t[10] = State[2];
        t[14] = State[6];

        t[3] = State[15];
        t[7] = State[3];
        t[11] = State[7];
        t[15] = State[11];

        for(int i = 0; i <= t.length - 1; i++){
            State[i] = t[i];
        }
    }

    public void MixColumns(){
        byte b[] = new byte[4];
        //byte a[] = new byte[4];
        for(int i=0; i<4; i++) {
            byte[] a = {State[0+4*i],State[1+4*i],State[2+4*i],State[3+4*i]};

            State[0+4*i] = (byte) (gmul(a[0], (byte) 0x02) ^ gmul(a[1], (byte) 0x03) ^ gmul(a[2], (byte) 0x01) ^ gmul(a[3], (byte) 0x01));
            State[1+4*i] = (byte) (gmul(a[0], (byte) 0x01) ^ gmul(a[1], (byte) 0x02) ^ gmul(a[2], (byte) 0x03) ^ gmul(a[3], (byte) 0x01));
            State[2+4*i] = (byte) (gmul(a[0], (byte) 0x01) ^ gmul(a[1], (byte) 0x01) ^ gmul(a[2], (byte) 0x02) ^ gmul(a[3], (byte) 0x03));
            State[3+4*i] = (byte) (gmul(a[0], (byte) 0x03) ^ gmul(a[1], (byte) 0x01) ^ gmul(a[2], (byte) 0x01) ^ gmul(a[3], (byte) 0x02));
        }
    }
}

/*
  $ make
  javac *.java
  $ java Aes
  Le bloc "State" en entrée vaut :
            00 00 00 00
            00 00 00 00
            00 00 00 00
            00 00 00 00
  Le bloc "State" en sortie vaut :
            66 EF 88 CA
            E9 8A 4C 34
            4B 2C FA 2B
            D4 3B 59 2E
*/
