package xjtlu.tdes.client;

public class Main {
    public static void main(String[] args) throws Exception {
        CryptionModule.TDESEncryption("XJTLU-logo.png","testpasswd", new byte[8], "2022-03-18 00:00:00");
        CryptionModule.TDESDecryption("XJTLU-logo.png","testpasswd", new byte[8]);
    }
}
