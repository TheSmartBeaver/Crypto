// -*- coding: utf-8 -*-

public class MonRC4
{     
    private static int LG_FLUX = 10;
    // Ce programme ne produira que les 10 premiers octets de la clef longue.

    static int[] clef = {1, 2, 3, 4, 5};
    // static int[] clef = {0x4B, 0x59, 0x4F, 0x54, 0x4F}; // C'est "KYOTO"

    
    static int[] state = new int[256];       // Ces int sont <256.
    static int i = 0, j = 0;                 // Ils représentent un octet.

    public static void main(String[] args)
    {
        /*TODO: Modifier ce prog pour faire un XOR. Mais j'ai pas envie tout de suite, je préfère l'AES*/
        initialisation();
        System.out.print("Premiers octets de la clef longue : ");
        for (int k = 0; k < LG_FLUX; k++) { /**LG_FLUX définit le nombre de 1ers octets à produire pour clef longue ! Le changer pour créer clef plus longue ?? **/
            System.out.printf("0x%02X ", production());
            // Affichage d'un octet généré en hexadécimal
        }
        System.out.print("\n");
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

