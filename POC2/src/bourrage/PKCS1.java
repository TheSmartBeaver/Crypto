package bourrage;

public class PKCS1 {

    /*Bourrage PKCS1*/
    public static byte[] getPkcs1_Of(byte[] m){
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
}
