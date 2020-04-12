package bourrage;
import utils.Utils;

public class PKCS5 {

    public static void main(String[] args) {
        //getPkcs5OfFile("butokuden.jpg",16,"pkcs5-butokuden.jpg");
    }

    /**
     *
     * @param path
     * @param k entier multiple à la taille du fichier (16 pour l'AES car blocs sont de tailles 16)
     */
    public static byte[] getPkcs5OfFile(String path, int k){
        //File sortie = new File(path_fic_sortie);


        byte[] file_original = Utils.getBytesOfFile(path);
        int nbOctets_fichier = Utils.countOctetOfFile(path);
        System.out.println("Le fichier possède "+nbOctets_fichier+" octets.");

        int nbOctetsSuplémentaire = k - (nbOctets_fichier%k);
        return getBytesPadded_With_PKCS5(file_original,nbOctetsSuplémentaire);
        //utils.Utils.writeBytesInFile(getBytesPadded_With_PKCS5(file_original,nbOctetsSuplémentaire),sortie);
    }

    private static byte[] getBytesPadded_With_PKCS5(byte[] array, int nb_octet_supp){
        byte[] padded_array = new byte[array.length+nb_octet_supp];
        byte added = getCorrespondingByteForInt(nb_octet_supp);
        for(int i=0; i<array.length; i++){
            padded_array[i]=array[i];
        }

        for(int i=array.length; i<array.length + nb_octet_supp; i++){
            padded_array[i]=added;
        }

        return padded_array;
    }

    private static byte getCorrespondingByteForInt(int i){
        switch (i) {
            case 0: return 0x00;
            case 1: return 0x01;
            case 2: return 0x02;
            case 3: return 0x03;
            case 4: return 0x04;
            case 5: return 0x05;
            case 6: return 0x06;
            case 7: return 0x07;
            case 8: return 0x08;
            case 9: return 0x09;
            case 10: return 0x0a;
            case 11: return 0x0b;
            case 12: return 0x0c;
            case 13: return 0x0d;
            case 14: return 0x0e;
            case 15: return 0x0f;

        }
        return -1;
    }

}
