// -*- coding: utf-8 -*-

import java.io.*;
import java.security.*;

public class ResumeSHA1 {
    public static void main(String[] args) throws Exception {
        byte[] resumeSHA1;
      	String message = "Alain Turin";
        System.out.println("Message à hacher: \"" + message +"\"");

        MessageDigest fonctionDeHachage = MessageDigest.getInstance("SHA1");
        resumeSHA1 = fonctionDeHachage.digest(message.getBytes());
        
        System.out.print("Le résumé SHA1 de cette chaîne est: 0x");
        for(byte octet: resumeSHA1) System.out.print(String.format("%02X", octet));
        System.out.println();
    }
}

/* 
   $ javac ResumeSHA1.java 
   $ java ResumeSHA1
   Message à hacher: "Alain Turin"
   Le résumé SHA1 de cette chaîne est: 0x9B682F2CA6F44CB60493288A686DE5D81ECA6B6D
*/

