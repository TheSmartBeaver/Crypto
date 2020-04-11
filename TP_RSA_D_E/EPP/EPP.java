// -*- coding: utf-8 -*-

import java.math.BigInteger;
import java.util.Random;

public class EPP
{
    private static int certainty = 2147483647; /*MAX_INTEGER*/
    public static void main(String[] args)
    {
        BigInteger n = new BigInteger("170141183460469231731687303715884105727", 10);
        int i=0;
        float err_probality = (1f - ((1f/2f)*Float.parseFloat(Integer.toString(certainty))));


        System.out.print("Le nombre " + n + "avec une probabilité de "+ err_probality);
        if (est_probablement_premier(n))
            System.out.println(" est très probablement premier!");
        else
            System.out.println(" n'est absolument pas premier!");

        find514BitsKey();
    }

    static boolean est_probablement_premier(BigInteger n)
    {
        /*
          Modifiez cette fonction afin qu'elle retourne si oui
          ou non l'entier n est un nombre premier, avec un taux 
          d'erreur inférieur à 1/1000 000 000 000 000 000.
        */
        return n.isProbablePrime(certainty);
    }

    static BigInteger find514BitsKey(){
        Random graine = new Random();
        BigInteger result = new BigInteger(514, 2147483647, graine);

        if(result.isProbablePrime(2147483647))
            System.out.println("OK !!");
        else
            System.out.println("Pas OK");

        return result;
    }
}

/*
  $ make
  javac *.java 
  $ java EPP
  Le nombre 170141183460469231731687303715884105727 ...
*/

