// -*- coding: utf-8 -*-

import java.io.*;
import java.nio.ByteBuffer;

import static java.lang.System.exit;

public class MonRC4
{
    private static int LG_FLUX = 10;
    // Ce programme ne produira que les 10 premiers octets de la clef longue.

    //static int[] clef = {1, 2, 3, 4, 5};
    static int[] clef = {0x4B, 0x59, 0x4F, 0x54, 0x4F}; // C'est "KYOTO"


    static int[] state = new int[256];       // Ces int sont <256.
    static int i = 0, j = 0;                 // Ils représentent un octet.

    static int iW =0; /*Cette var va nous indiqué l'indice courant de la clé longue*/

    //static String fileName = "texte.txt";
    //static String fichierSortie = "output.txt";

    static String fileName = "output.jpg";
    static String fichierSortie = "output2.jpg";


    public static void XOR(String nameFile, int[] W, int fileLength) throws IOException {
        int nbOctetsLus = 0;
        try {
            File fichier = new File(nameFile);
            FileInputStream fis = new FileInputStream(fichier);
            System.out.println("taille du fichier en octets : "+fileLength);
            byte[] cryptedFile = new byte[fileLength];

            byte[] buffer = new byte[1024];
            nbOctetsLus = fis.read(buffer);                   // Lecture du premier morceau
            int pointeurFichier = 0;

            /*On commence à lire le fichier à crypter*/
            while (nbOctetsLus != -1) {

                addBlocToFinalFile(cryptedFile, XORBuffer(buffer,W, pointeurFichier,nbOctetsLus), pointeurFichier, nbOctetsLus);
                pointeurFichier+=nbOctetsLus;
                System.out.println("pointeurFichier "+pointeurFichier);
                //System.out.println("Nous avons lu en tout "+pointeurFichier +" octets");
                nbOctetsLus = fis.read(buffer);                   // Lecture du morceau suivant
            }
            fis.close();
            //for(byte gg : cryptedFile)
            //System.out.println(gg + " ");
            /*On écrit le nouveau fichier*/
            File ficSortie = new File(fichierSortie);
            Utils.writeBytesInFile(cryptedFile,ficSortie);
        } catch (Exception e) { e.printStackTrace(); }

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

    public static byte[] XORBuffer(byte[] buffer, int[] W, int filePointer, int nbOctetLus){
        byte cryptedBuffer[] = new byte[buffer.length];
        //System.out.println("Le buffer a une taille de "+buffer.length);
        int indice_buffer;
        for(indice_buffer=0; indice_buffer<nbOctetLus;) {
            //System.out.println("iW="+iW+" indiceBuffer="+indice_buffer);
            int wi = W[iW];
            /*On obtient 4 octet car un int fait 32 bits soit 4 octets*/
            byte[] bytes = ByteBuffer.allocate(4).putInt(wi).array();
            for (int j=0;j<4;j++){
                byte b = XORTwoBytes(buffer[indice_buffer],bytes[j]);
                cryptedBuffer[indice_buffer] = b;
                indice_buffer++;
            }
            iW++;
        }
        //System.out.println("indice buffer devrait être égale à 1024, = "+indice_buffer);
        return cryptedBuffer;
    }


    public static void addBlocToFinalFile(byte[] finalFile, byte[] morceau, int start, int nbOctetsLus){
        int j=0;
        /*for(byte b : morceau)
            System.out.print(b+" ");*/
        for(int i=start; i< (start+nbOctetsLus); i++){ //TODO: Le problème est ici !
            System.out.println("i="+i+" j="+j+" pointeur="+nbOctetsLus);
            finalFile[i] = morceau[j];
            j++;
        }
        /*for(byte b : finalFile)
            System.out.print(b+" ");*/
    }

    public static void main(String[] args) throws IOException {


        //:TODO Le +1024 est fait pour débuguer ?! A enlever
        LG_FLUX = (Utils.countOctetOfFile(fileName) / 4) + 1024; /**On divise par 4 car 32(int)/4 = 1 byte*/
        int W[] = new int[LG_FLUX];
        System.out.println("La clé W est de taille "+W.length);
        initialisation();
        System.out.print("Premiers octets de la clef longue : ");
        for (int k = 0; k < LG_FLUX; k++) { /**LG_FLUX définit le nombre de 1ers octets à produire pour clef longue ! Le changer pour créer clef plus longue ?? **/
            int w = production();
            System.out.printf("0x%02X ", w);
            W[k] = w;
            // Affichage d'un octet généré en hexadécimal
        }
        System.out.print("\n");

        XOR(fileName, W, Utils.countOctetOfFile(fileName));
        System.out.println("valeur finale de iW ! "+iW);

    }

    private static void échange(int k, int l)
    {
        int temp = state[k];
        state[k] = state[l];
        state[l] = temp;
    }

    private static void initialisation()
    {
        /*1ère partie p6 phase d'initialisisation*/
        int shortKeyLength = clef.length;

        System.out.print("Clef courte utilisée : ");
        for (int k = 0; k < shortKeyLength ; k++ )
            System.out.print(String.format("0x%02X ", clef[k]));
        System.out.println("\nLongueur de la clef courte : " + shortKeyLength);

        for (i=0; i < 256; i++) state[i] = i; /*On remplit state[0]=0, ..., state[255]=255*/
        j = 0;
        /** On modifie le tableau S (state) en usant de la clef courte**/
        for (int i=0; i < 256; i++) {
            j = (j + state[i] + clef[i % shortKeyLength]) % 256;
            échange(i,j);                // Echange des octets en i et j
        }
        i = 0; /*car i et j sont des variables de la classe et non locale*/
        j = 0;
    }

    /**Cette fonction retourne l'octet numéro "i" de la clef longue **/
    private static int production()
    {
        /*2ème simple partie p6 phase d'initialisisation*/
        i = (i + 1) % 256;            // Incrémentation de i modulo 256
        j = (j + state[i]) % 256;     // Déplacement de j
        échange(i,j);                 // Echange des octets en i et j
        return state[(state[i] + state[j]) % 256];
    }
}

/* 1er test avec la clef 12345
  $ make
  javac *.java 
  $ java MonRC4
  Clef courte utilisée : 0x01 0x02 0x03 0x04 0x05 
  Longueur de la clef courte : 5
  Premiers octets de la clef longue : 0xB2 0x39 0x63 0x05 0xF0 0x3D 0xC0 0x27 0xCC 0xC3 
*/

/* 2nd test avec la clef "KYOTO"
  $ make
  javac *.java 
  $ java MonRC4
  Clef courte utilisée : 0x4B 0x59 0x4F 0x54 0x4F 
  Longueur de la clef courte : 5
  Premiers octets de la clef longue : 0x8C 0xE5 0x01 0xB4 0xD3 0xDF 0x6B 0xA7 0x41 0x0D 
*/

/*
  Pour trouver le codage de "KYOTO" en hexadécimal:
  $ echo -n "KYOTO" | od -t x1
  0000000    4b  59  4f  54  4f                                            
  0000005
*/