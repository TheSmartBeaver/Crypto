public class CBC {
    public static void encrypt_file_with_CBC(String path, int taille_bloc){
        byte[] bytesOfFile = Utils.getBytesOfFile(path);
        int nb_blocs = bytesOfFile.length/taille_bloc;
        byte IV[] = new byte[taille_bloc];
        byte c1[] = new byte[taille_bloc];

    }

    public byte[] xorBlocs(byte[] a, byte[] b){
        byte[] result = new byte[a.length];
        for (int i=0; i<a.length; i++)
            result[i] = Utils.XORTwoBytes(a[i],b[i]);
        return result;
    }
}
