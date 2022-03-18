package xjtlu.tdes.server.utils;

import java.security.SecureRandom;

public class SaltUtil {
    //private static final String alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final char[] alphanum = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'
            , 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't'
            , 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static String saltGeneration(int len) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < len; i++) {
            stringBuilder.append(alphanum[new SecureRandom().nextInt(62)]);
        }
        return stringBuilder.toString();
    }

    public static byte[] ivGeneration() {
        return saltGeneration(8).getBytes();
    }
}